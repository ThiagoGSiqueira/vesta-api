package com.vesta.vesta_api.domain.event.repository;

import com.vesta.vesta_api.domain.event.model.Event;
import com.vesta.vesta_api.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUser(User user);
}
