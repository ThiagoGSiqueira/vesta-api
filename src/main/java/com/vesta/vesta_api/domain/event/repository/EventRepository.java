package com.vesta.vesta_api.domain.event.repository;

import com.vesta.vesta_api.domain.event.model.Event;
import com.vesta.vesta_api.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByIdAndUser(Long id, User user);
    List<Event> findAllByUser(User user);
}
