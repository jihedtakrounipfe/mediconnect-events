package com.mediconnect.events.repository;

import com.mediconnect.events.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, UUID> {

    // ✅ check if user already registered
    boolean existsByEventIdAndUserId(UUID eventId, UUID userId);

    // ✅ get specific registration (used in cancel)
    Optional<EventRegistration> findByEventIdAndUserId(UUID eventId, UUID userId);

    // ✅ get all participants
    List<EventRegistration> findByEventId(UUID eventId);
}