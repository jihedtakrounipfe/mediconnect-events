package com.mediconnect.events.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final EmailService emailService;

    @KafkaListener(topics = "user-promoted-topic", groupId = "notification-group")
    public void listen(String message) {

        System.out.println("📩 " + message);

        // 🔥 TEMP EMAIL (for test)
        String email = "raouftakrouni0@gmail.com";

        // extract eventId from message (simple way)
        String eventId = message;

        emailService.sendPromotionEmail(email, eventId);
    }
}