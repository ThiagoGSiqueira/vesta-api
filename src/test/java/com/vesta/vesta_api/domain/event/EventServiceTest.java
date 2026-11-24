package com.vesta.vesta_api.domain.event;

import com.vesta.vesta_api.domain.event.dto.EventCreateDTO;
import com.vesta.vesta_api.domain.event.dto.EventResponseDTO;
import com.vesta.vesta_api.domain.event.mapper.EventMapper;
import com.vesta.vesta_api.domain.event.model.Event;
import com.vesta.vesta_api.domain.event.model.EventStatus;
import com.vesta.vesta_api.domain.event.repository.EventRepository;
import com.vesta.vesta_api.domain.event.service.EventService;
import com.vesta.vesta_api.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Captor
    private ArgumentCaptor<Event> eventCaptor;

    @Mock
    private EventRepository eventRepository;

    @Spy
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    @Test
    void shouldCreateEvent() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        EventCreateDTO request = new EventCreateDTO("clientTest", "00000000000", LocalDate.parse("01/01/2027", formatter));
        User user = User.builder().name("userTest").email("test@gmail.com").password("test").build();

        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
            Event event = invocation.getArgument(0);
            ReflectionTestUtils.setField(event, "id", 1L);
            return event;
        });

        EventResponseDTO response = eventService.createEvent(request, user);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("clientTest", response.clientName());
        assertEquals(BigDecimal.ZERO, response.totalPrice());
        assertEquals("00000000000", response.contact());
        assertEquals("wa.me/00000000000", response.contactLink());
        assertEquals(LocalDate.parse("01/01/2027", formatter), response.scheduledDate());
        assertEquals(EventStatus.QUOTE, response.status());
        verify(eventRepository).save(eventCaptor.capture());
        Event eventSaved = eventCaptor.getValue();
        assertEquals(user.getEmail(), eventSaved.getUser().getEmail());
    }

    @Test
    void shouldReturnEventResponseDTO() {
        //AAA (Preparar, Agir, Verificar)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        User user = User.builder().name("userTest").email("test@gmail.com").password("test").build();
        Event event = Event.builder().clientName("clientTest").user(user).contact("00000000000").scheduledDate(LocalDate.parse("01/01/2027", formatter)).build();
        ReflectionTestUtils.setField(event, "id", 1L);

        when(eventRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(event));

        EventResponseDTO response = eventService.getById(event.getId(), user);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("wa.me/00000000000", response.contactLink());
        assertEquals(BigDecimal.ZERO, response.totalPrice());
        assertEquals(EventStatus.QUOTE, response.status());
    }
}
