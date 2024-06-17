package com.example.kafkafundamentals;

import static java.time.Duration.ofSeconds;
import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.example.kafkafundamentals.KafkafundamentalsApplication.createJavalinApp;
import static com.example.kafkafundamentals.util.TestKafkaHelpers.transactionConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import com.example.kafkafundamentals.dto.Transaction;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;

@Testcontainers
class KafkafundamentalsApplicationTest {

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
    void shouldSendTransactions() {
        final Transaction expected = Transaction.builder().bank("Bank").build();
        JavalinTest.test(app, (app, client) -> {
            final Response response = client.post("/transactions", expected);
            final List<Transaction> actual = transactionEvents();

            assertTrue(response.isSuccessful());
            assertThat(actual.size(), equalTo(1));
            assertThat(actual.get(0), equalTo(expected));
        });
    }

    @NotNull
    private static List<Transaction> transactionEvents() {
        final List<Transaction> list = new ArrayList<>();
        try (Consumer<String, Transaction> cons = transactionConsumer(kafka.getBootstrapServers(),
                "shouldSendTransactions")) {
            cons.subscribe(singleton(topic));
            cons.poll(ofSeconds(1)).forEach(r -> list.add(r.value()));
        }
        return list;
    }
}
