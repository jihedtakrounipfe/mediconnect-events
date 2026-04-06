package com.mediconnect.events.repository;

import com.mediconnect.events.entity.EventWaitingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventWaitingListRepository extends JpaRepository<EventWaitingList, UUID> {

    List<EventWaitingList> findByEventIdOrderByCreatedAtAsc(UUID eventId);

    boolean existsByEventIdAndUserId(UUID eventId, UUID userId);
}