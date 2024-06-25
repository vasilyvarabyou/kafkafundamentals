package com.example.kafkafundamentals.producer.handlers;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.example.kafkafundamentals.producer.dto.Transaction;
import com.example.kafkafundamentals.producer.serdeser.JsonSerializer;

public class KafkaHelpers {

    public static Producer<String, Transaction> transactionProducer(final String servers) {
        final Properties properties = new Properties();
        properties.setProperty(CLIENT_ID_CONFIG, "mytopic-producer");
        properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, servers);
        properties.setProperty(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
        properties.setProperty(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getCanonicalName());
        properties.setProperty(COMPRESSION_TYPE_CONFIG, "none");
        properties.setProperty(ACKS_CONFIG, "1");
        properties.setProperty("batch.size", "0");
        return new KafkaProducer<>(properties);

    }

}
