package com.mediconnect.events.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID organizerId;

    private String title;

    @Column(length = 2000)
    private String description;

    private String location;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Integer maxParticipants;

    @Builder.Default
    private Integer registeredCount = 0;

    private Boolean isPublished = false;

    @Transient
    public String getStatus() {
        LocalDateTime now = LocalDateTime.now();

        int count = registeredCount == null ? 0 : registeredCount;
        int max = maxParticipants == null ? 0 : maxParticipants;

        if (count >= max && max > 0) return "FULL";

        if (startDate != null && now.isBefore(startDate)) return "UPCOMING";

        if (endDate != null && now.isAfter(endDate)) return "COMPLETED";

        return "ONGOING";
    }
}