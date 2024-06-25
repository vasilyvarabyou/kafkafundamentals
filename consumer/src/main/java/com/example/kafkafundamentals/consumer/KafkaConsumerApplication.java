package com.example.kafkafundamentals.consumer;

import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;

import com.example.kafkafundamentals.consumer.dto.Transaction;
import com.example.kafkafundamentals.consumer.handlers.KafkaHelpers;
import com.example.kafkafundamentals.consumer.handlers.TransactionsHandler;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import io.javalin.Javalin;

public class KafkaConsumerApplication {

    public static void main(String[] args) {
        createJavalinApp(ConfigFactory.load()).start(8080);
    }

    public static Javalin createJavalinApp(final Config config) {

        final String server = config.getString("kafka.bootstrap.servers");
        final String topic = config.getString("kafka.topic");
        final Consumer<String, Transaction> consumer = KafkaHelpers.transactionConsumer(server);
        consumer.subscribe(List.of(topic));
        final TransactionsHandler transactionsHandler = TransactionsHandler.create(consumer);

        return Javalin.create(javalinConfig -> {
            javalinConfig.events.serverStopped(consumer::close);})
                .get("/transactions", transactionsHandler::getTransactions);
    }
}
