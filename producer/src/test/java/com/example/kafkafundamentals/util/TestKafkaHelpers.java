package com.example.kafkafundamentals.util;

import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.example.kafkafundamentals.producer.dto.Transaction;
import com.example.kafkafundamentals.serdeser.JsonDeserializer;

public class TestKafkaHelpers {

    public static Consumer<String, Transaction> transactionConsumer(final String servers, final String consumerGroupId) {
        final Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getCanonicalName());
        properties.put("JsonClass", Transaction.class);
        return new KafkaConsumer<>(properties);
    }
}
