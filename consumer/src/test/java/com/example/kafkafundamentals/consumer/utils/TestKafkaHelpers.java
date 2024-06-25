package com.example.kafkafundamentals.consumer.utils;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import com.example.kafkafundamentals.consumer.dto.Transaction;

public class TestKafkaHelpers {

    public static Producer<String, Transaction> transactionProducer(final String servers) {
        final Properties properties = new Properties();
        properties.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "test-producer");
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getCanonicalName());
        // properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        // properties.setProperty("batch.size", "0");
        return new KafkaProducer<>(properties);

    }

}
