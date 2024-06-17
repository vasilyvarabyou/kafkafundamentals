package com.example.kafkafundamentals;

import org.apache.kafka.clients.producer.Producer;
import org.jetbrains.annotations.NotNull;
import com.example.kafkafundamentals.dto.Transaction;
import com.example.kafkafundamentals.handlers.KafkaHelpers;
import com.example.kafkafundamentals.handlers.TransactionsHandler;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.javalin.Javalin;

public class KafkafundamentalsApplication {

    public static void main(String[] args) {
        createJavalinApp(ConfigFactory.load()).start(8080);
    }

    @NotNull
    public static Javalin createJavalinApp(final Config config) {

        final String server = config.getString("kafka.bootstrap.servers");
        final String topic = config.getString("kafka.topic");
        final Producer<String, Transaction> producer = KafkaHelpers.transactionProducer(server);
        final TransactionsHandler transactionsHandler = TransactionsHandler.create(producer, topic);

        return Javalin.create(javalinConfig -> javalinConfig.events.serverStopped(producer::close))
                .post("/transactions", transactionsHandler::create);
    }

}
