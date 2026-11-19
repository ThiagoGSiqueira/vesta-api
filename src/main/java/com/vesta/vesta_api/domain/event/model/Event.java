package com.vesta.vesta_api.domain.event.model;

import com.vesta.vesta_api.domain.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String clientName;

    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    private String contact;

    @Column(nullable = false)
    private String contactLink;

    @Column(nullable = false)
    private LocalDate scheduledDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @Builder
    public Event(String clientName, User user, String contact, LocalDate scheduledDate) {
        this.clientName = clientName;
        this.totalPrice = BigDecimal.ZERO;
        this.user = user;
        this.contact = contact;
        this.contactLink = "wa.me/" + contact;
        this.scheduledDate = scheduledDate;
        this.status = EventStatus.QUOTE;
    }

    public void renameClient(String name) {
        if (name != null && !name.isBlank()) {
            this.clientName = name;
        }
    }

    public void changeContact(String contact) {
        if (contact != null && !contact.isBlank()) {
            this.contact = contact;
            this.contactLink = "wa.me/" + contact;
        }
    }

    public void reschedule(LocalDate scheduledDate) {
        if (scheduledDate != null) {
            this.scheduledDate = scheduledDate;
        }
    }
}
