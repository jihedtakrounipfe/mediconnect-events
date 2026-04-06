package com.mediconnect.events.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {

    public void notifyUserPromoted(UUID userId, UUID eventId) {

        // 🔥 for now just log
        System.out.println("🔔 User " + userId + " promoted to event " + eventId);

        // later:
        // send email / websocket / sms
    }
}