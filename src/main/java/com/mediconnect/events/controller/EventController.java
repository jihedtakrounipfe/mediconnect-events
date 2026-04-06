package com.mediconnect.events.controller;

import com.mediconnect.events.dto.HealthEventDto;
import com.mediconnect.events.service.HealthEventService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class EventController {

    private final HealthEventService service;

    // CREATE
    @Operation(summary = "Create event (ADMIN)")
    @PostMapping
    public HealthEventDto create(@RequestBody HealthEventDto dto) {
        return service.create(dto);
    }

    // GET ALL
    @Operation(summary = "Get events with filters + pagination")
    @GetMapping
    public Page<HealthEventDto> getAll(
            @Parameter(description = "Title filter")
            @RequestParam(required = false) String title,

            @Parameter(description = "Location filter")
            @RequestParam(required = false) String location,

            @Parameter(description = "Published filter")
            @RequestParam(required = false) Boolean isPublished,

            Pageable pageable
    ) {
        return service.getAll(title, location, isPublished, pageable);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public HealthEventDto getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public HealthEventDto update(@PathVariable UUID id,
                                 @RequestBody HealthEventDto dto) {
        return service.update(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable UUID id) {
        service.delete(id);
        return "Event deleted successfully";
    }

    // REGISTER
    @PostMapping("/{eventId}/register")
    public String register(@PathVariable UUID eventId,
                           @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());
        return service.register(eventId, userId);
    }

    // CANCEL
    @DeleteMapping("/{eventId}/cancel")
    public String cancel(@PathVariable UUID eventId,
                         @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getSubject());
        return service.cancelRegistration(eventId, userId);
    }

}