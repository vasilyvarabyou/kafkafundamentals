package com.example.kafkafundamentals.consumer.handlers;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.example.kafkafundamentals.consumer.dto.Transaction;
import com.example.kafkafundamentals.consumer.serdeser.JsonDeserializer;

public class KafkaHelpers {

    public static Consumer<String, Transaction> transactionConsumer(final String servers) {
        try {
            final Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, Inet4Address.getLocalHost().getHostName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "mytopic-consumers-group");
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getCanonicalName());
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            properties.put("JsonClass", Transaction.class);
            return new KafkaConsumer<>(properties);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Unable to set up kafka consumer", e);
        }
    }

}
