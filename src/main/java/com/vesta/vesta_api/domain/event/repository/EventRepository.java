package com.vesta.vesta_api.domain.event.repository;

import com.vesta.vesta_api.domain.event.model.Event;
import com.vesta.vesta_api.domain.event.model.EventStatus;
import com.vesta.vesta_api.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndUser(Long id, User user);
    List<Event> findAllByUser(User user);
    List<Event> findAllByUserAndStatusNot(User user, EventStatus status);
    List<Event> findAllByUserAndStatus(User user, EventStatus status);
}
