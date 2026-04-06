package com.mediconnect.events.mapper;

import com.mediconnect.events.dto.HealthEventDto;
import com.mediconnect.events.entity.HealthEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Builder;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false))
public interface HealthEventMapper {

    default HealthEventDto toDto(HealthEvent entity) {
        if (entity == null) return null;

        HealthEventDto dto = new HealthEventDto();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setLocation(entity.getLocation());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setMaxParticipants(entity.getMaxParticipants());
        dto.setIsPublished(entity.getIsPublished());

        // 🔥 SAFE STATUS
        dto.setStatus(entity.getStatus());

        return dto;
    }

    HealthEvent toEntity(HealthEventDto dto);
}