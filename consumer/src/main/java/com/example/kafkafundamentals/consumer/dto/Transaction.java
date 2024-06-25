package com.example.kafkafundamentals.consumer.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record Transaction(
        String bank,
        Long clientId,
        TransactionType orderType,
        Integer quantity,
        Double price,
        LocalDateTime createdAt) {
}
