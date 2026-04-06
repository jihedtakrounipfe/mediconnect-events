package com.mediconnect.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendUserPromoted(UUID userId, UUID eventId) {

        String message = "User promoted: " + userId + " for event: " + eventId;

        kafkaTemplate.send("user-promoted-topic", message);
    }
}