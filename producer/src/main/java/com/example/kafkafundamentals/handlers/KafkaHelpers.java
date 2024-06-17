package com.example.kafkafundamentals.handlers;

import static org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.COMPRESSION_TYPE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;
import com.example.kafkafundamentals.dto.Transaction;
import com.example.kafkafundamentals.serdeser.JsonSerializer;

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
