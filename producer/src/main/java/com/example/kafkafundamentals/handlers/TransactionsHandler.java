package com.example.kafkafundamentals.handlers;

import static java.util.Map.of;
import static io.javalin.http.HttpStatus.ACCEPTED;
import static io.javalin.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static lombok.AccessLevel.PRIVATE;

import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.NotNull;
import com.example.kafkafundamentals.dto.Transaction;
import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public class TransactionsHandler {

    private final String topic;
    private final Producer<String, Transaction> producer;

    public void create(final @NotNull Context ctx) throws ExecutionException, InterruptedException {
        producer.send(new ProducerRecord<>(topic, "key", ctx.bodyAsClass(Transaction.class)), (recordMetadata, e) -> {
            if (e != null) {
                ctx.status(INTERNAL_SERVER_ERROR).json(of("errorMessage", e.getMessage()));
            } else {
                ctx.status(ACCEPTED);
            }
        }).get();
    }

    public static TransactionsHandler create(final Producer<String, Transaction> producer, final String topic) {
        return new TransactionsHandler(topic, producer);
    }
}
