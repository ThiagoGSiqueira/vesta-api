package com.vesta.vesta_api.domain.event.repository;

import com.vesta.vesta_api.domain.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
