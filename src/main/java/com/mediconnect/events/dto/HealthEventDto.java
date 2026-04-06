package com.mediconnect.events.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthEventDto {

    private UUID id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxParticipants;
    private Boolean isPublished;
    private String status;

}
