package com.mediconnect.events.specification;

import com.mediconnect.events.entity.HealthEvent;
import org.springframework.data.jpa.domain.Specification;

public class HealthEventSpecification {

    public static Specification<HealthEvent> hasTitle(String title) {
        return (root, query, cb) ->
                title == null ? null :
                        cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<HealthEvent> hasLocation(String location) {
        return (root, query, cb) ->
                location == null ? null :
                        cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
    }

    public static Specification<HealthEvent> isPublished(Boolean isPublished) {
        return (root, query, cb) ->
                isPublished == null ? null :
                        cb.equal(root.get("isPublished"), isPublished);
    }
}