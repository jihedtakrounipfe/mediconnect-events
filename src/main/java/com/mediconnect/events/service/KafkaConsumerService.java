package com.mediconnect.events.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "user-promoted-topic", groupId = "notification-group")
    public void consume(String message) {

        System.out.println("📩 " + message);

    }
}