package com.example.kafkafundamentals.consumer.handlers;

import static io.javalin.http.HttpStatus.*;
import static java.time.Duration.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.*;
import static lombok.AccessLevel.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;

import com.example.kafkafundamentals.consumer.dto.Transaction;

import io.javalin.http.Context;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public class TransactionsHandler {

    private final Consumer<String, Transaction> consumer;

    public void getTransactions(final @NotNull Context ctx) throws ExecutionException, InterruptedException {
        final Iterable<ConsumerRecord<String, Transaction>> records = consumer.poll(ofMillis(100));
        final List<Transaction> result = stream(records.spliterator(), false).map(r -> r.value()).collect(toList());
        ctx.json(result).status(result.isEmpty() ? NO_CONTENT : OK);
    }

    public static TransactionsHandler create(final Consumer<String, Transaction> consumer) {
        return new TransactionsHandler(consumer);
    }
}
