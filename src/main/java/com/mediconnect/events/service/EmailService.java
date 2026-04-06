package com.mediconnect.events.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPromotionEmail(String toEmail, String eventId) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("🎉 You have been promoted!");

        message.setText(
                "Good news!\n\n" +
                        "You have been promoted from the waiting list.\n" +
                        "Event ID: " + eventId + "\n\n" +
                        "See you there!"
        );

        mailSender.send(message);
    }
}