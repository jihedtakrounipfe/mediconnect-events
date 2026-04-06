package com.mediconnect.events.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventWaitingList {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID eventId;
    private UUID userId;

    private LocalDateTime createdAt;
}