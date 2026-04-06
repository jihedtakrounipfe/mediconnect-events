package com.mediconnect.events.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.mediconnect.events.entity.HealthEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.UUID;
@Repository
public interface HealthEventRepository
        extends JpaRepository<HealthEvent, UUID>,
        JpaSpecificationExecutor<HealthEvent> {
    Page<HealthEvent> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<HealthEvent> findByLocationContainingIgnoreCase(String location, Pageable pageable);

    Page<HealthEvent> findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(
            String title, String location, Pageable pageable);
}