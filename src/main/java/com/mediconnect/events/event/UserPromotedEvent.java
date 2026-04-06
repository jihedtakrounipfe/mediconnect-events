package com.mediconnect.events.event;

import lombok.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPromotedEvent {
    private UUID userId;
    private UUID eventId;
}