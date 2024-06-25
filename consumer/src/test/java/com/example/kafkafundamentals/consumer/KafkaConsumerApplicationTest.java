package com.example.kafkafundamentals.consumer;

import static com.example.kafkafundamentals.consumer.KafkaConsumerApplication.*;
import static io.javalin.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import com.example.kafkafundamentals.consumer.dto.Transaction;
import com.example.kafkafundamentals.consumer.utils.TestKafkaHelpers;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;

@Testcontainers
public class KafkaConsumerApplicationTest {
    @Container
    private static final KafkaContainer kafka = new KafkaContainer("apache/kafka");
    private static Javalin app;
    private static String topic;

    @BeforeAll
    static void setUp() throws ExecutionException, InterruptedException {
        System.setProperty("KAFKA_BOOTSTRAP_SERVERS", kafka.getBootstrapServers());
        final Config config = ConfigFactory.load();
        topic = config.getString("kafka.topic");
        createTopic(kafka.getBootstrapServers(), topic);

        app = createJavalinApp(config);
    }

    private static void createTopic(final String bootstrapServers, final String topic)
            throws InterruptedException, ExecutionException {
        try (AdminClient adminClient = AdminClient.create(Map.of("bootstrap.servers", bootstrapServers))) {
            adminClient.createTopics(List.of(new NewTopic(topic, 1, (short) 1))).all().get();
            adminClient.listTopics().names().get().forEach(System.out::println);
        }
    }

    @Test
    void shouldGetTransactionFromTopic() throws InterruptedException, ExecutionException {
        Transaction expected = Transaction.builder().bank("Bank").build();

        JavalinTest.test(app, (app, client) -> {
            try (Producer<String, Transaction> producer = TestKafkaHelpers.transactionProducer(kafka.getBootstrapServers());) {
                producer.send(new ProducerRecord<String,Transaction>(topic, expected)).get();
            }
            Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
                final Response response = client.get("/transactions");
                if (response.code() == NO_CONTENT.getCode()) {
                    return false;
                }
                assertTrue(response.isSuccessful());
                JSONAssert.assertEquals("[{\"bank\": \"Bank\"}]", response.body().string(), false);
                return true;
            });
        });
    }
}
