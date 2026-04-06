package com.mediconnect.events.service;
import com.mediconnect.events.event.UserPromotedEvent;
import com.mediconnect.events.dto.HealthEventDto;
import com.mediconnect.events.entity.HealthEvent;
import com.mediconnect.events.entity.EventRegistration;
import com.mediconnect.events.entity.EventWaitingList;

import com.mediconnect.events.repository.HealthEventRepository;
import com.mediconnect.events.repository.EventRegistrationRepository;
import com.mediconnect.events.repository.EventWaitingListRepository;

import com.mediconnect.events.mapper.HealthEventMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static com.mediconnect.events.specification.HealthEventSpecification.*;

@Service
@RequiredArgsConstructor
public class HealthEventService {
    private final KafkaProducerService kafkaProducerService;
    private final HealthEventRepository repo;
    private final EventRegistrationRepository registrationRepo;
    private final EventWaitingListRepository waitingRepo;
    private final HealthEventMapper mapper;
    private final NotificationService notificationService;
    // ===============================
    // CREATE EVENT
    // ===============================
    public HealthEventDto create(HealthEventDto dto) {

        HealthEvent event = HealthEvent.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .maxParticipants(dto.getMaxParticipants())
                .isPublished(dto.getIsPublished())
                .registeredCount(0)
                .build();

        return mapper.toDto(repo.save(event));
    }

    // ===============================
    // GET ALL
    // ===============================
    public List<HealthEventDto> getAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    // ===============================
    // FILTER + PAGINATION
    // ===============================
    public Page<HealthEventDto> getAll(String title,
                                       String location,
                                       Boolean isPublished,
                                       Pageable pageable) {

        Specification<HealthEvent> spec = Specification
                .where(hasTitle(title))
                .and(hasLocation(location))
                .and(isPublished(isPublished));

        return repo.findAll(spec, pageable)
                .map(mapper::toDto);
    }

    // ===============================
    // REGISTER
    // ===============================
    public String register(UUID eventId, UUID userId) {

        HealthEvent event = repo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        int count = event.getRegisteredCount() == null ? 0 : event.getRegisteredCount();
        int max = event.getMaxParticipants() == null ? 0 : event.getMaxParticipants();

        if (registrationRepo.existsByEventIdAndUserId(eventId, userId)) {
            throw new RuntimeException("User already registered");
        }

        // 🔥 WAITING LIST
        if (count >= max && max > 0) {

            if (waitingRepo.existsByEventIdAndUserId(eventId, userId)) {
                throw new RuntimeException("Already in waiting list");
            }

            EventWaitingList waiting = EventWaitingList.builder()
                    .eventId(eventId)
                    .userId(userId)
                    .createdAt(LocalDateTime.now())
                    .build();

            waitingRepo.save(waiting);

            return "Event full → added to waiting list";
        }

        // NORMAL REGISTRATION
        EventRegistration registration = EventRegistration.builder()
                .eventId(eventId)
                .userId(userId)
                .registeredAt(LocalDateTime.now())
                .build();

        registrationRepo.save(registration);

        event.setRegisteredCount(count + 1);
        repo.save(event);

        return "User registered successfully";
    }

    // ===============================
    // CANCEL REGISTRATION
    // ===============================
    public String cancelRegistration(UUID eventId, UUID userId) {

        EventRegistration reg = registrationRepo
                .findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        registrationRepo.delete(reg);

        HealthEvent event = repo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        int count = event.getRegisteredCount() == null ? 0 : event.getRegisteredCount();
        event.setRegisteredCount(count - 1);

        // 🔥 AUTO PROMOTE
        List<EventWaitingList> waitingList =
                waitingRepo.findByEventIdOrderByCreatedAtAsc(eventId);

        if (!waitingList.isEmpty()) {
            EventWaitingList next = waitingList.get(0);

            EventRegistration newReg = EventRegistration.builder()
                    .eventId(eventId)
                    .userId(next.getUserId())
                    .registeredAt(LocalDateTime.now())
                    .build();
            kafkaProducerService.sendUserPromoted(next.getUserId(), eventId);
            registrationRepo.save(newReg);
            waitingRepo.delete(next);

            event.setRegisteredCount(event.getRegisteredCount() + 1);
        }

        repo.save(event);

        return "Registration cancelled successfully";
    }

    // ===============================
    // GET BY ID
    // ===============================
    public HealthEventDto getById(UUID id) {
        HealthEvent event = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return mapper.toDto(event);
    }

    // ===============================
    // UPDATE
    // ===============================
    public HealthEventDto update(UUID id, HealthEventDto dto) {
        HealthEvent event = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setStartDate(dto.getStartDate());
        event.setEndDate(dto.getEndDate());
        event.setMaxParticipants(dto.getMaxParticipants());
        event.setIsPublished(dto.getIsPublished());

        return mapper.toDto(repo.save(event));
    }

    // ===============================
    // DELETE
    // ===============================
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Event not found");
        }

        repo.deleteById(id);
    }
}