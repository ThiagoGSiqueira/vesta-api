package com.vesta.vesta_api.domain.event.service;

import com.vesta.vesta_api.domain.event.dto.EventCreateDTO;
import com.vesta.vesta_api.domain.event.dto.EventResponseDTO;
import com.vesta.vesta_api.domain.event.dto.EventUpdateDTO;
import com.vesta.vesta_api.domain.event.mapper.EventMapper;
import com.vesta.vesta_api.domain.event.model.Event;
import com.vesta.vesta_api.domain.event.model.EventStatus;
import com.vesta.vesta_api.domain.event.repository.EventRepository;
import com.vesta.vesta_api.domain.user.model.User;
import com.vesta.vesta_api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final User USER = User.builder().name("userTest").email("test@gmail.com").password("test").build();
    private static final String CLIENT_NAME = "clientTest";
    private static final String CONTACT = "12345678900";
    private static final LocalDate SCHEDULED_DATE = LocalDate.parse("01/01/2027", FORMATTER);

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
        EventCreateDTO request = new EventCreateDTO(CLIENT_NAME, CONTACT, SCHEDULED_DATE);

        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
            Event event = invocation.getArgument(0);
            ReflectionTestUtils.setField(event, "id", 1L);
            return event;
        });

        EventResponseDTO response = eventService.createEvent(request, USER);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(CLIENT_NAME, response.clientName());
        assertEquals("wa.me/12345678900", response.contactLink());
        verify(eventRepository).save(eventCaptor.capture());
        Event eventSaved = eventCaptor.getValue();
        assertEquals(USER.getEmail(), eventSaved.getUser().getEmail());
    }

    @Test
    void shouldReturnEventResponseDTO() {
        Event event = Event.builder().clientName(CLIENT_NAME).user(USER).contact(CONTACT).scheduledDate(SCHEDULED_DATE).build();
        ReflectionTestUtils.setField(event, "id", 1L);

        when(eventRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.of(event));

        EventResponseDTO response = eventService.getById(event.getId(), USER);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("wa.me/12345678900", response.contactLink());
        assertEquals(BigDecimal.ZERO, response.totalPrice());
        assertEquals(EventStatus.QUOTE, response.status());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenEventDoesNotExist() {
        when(eventRepository.findByIdAndUser(anyLong(), any(User.class))).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () ->
                eventService.getById(1L, USER)
        );

        assertEquals("Evento não encontrado.", e.getMessage());
    }

    @Test
    void shouldReturnEventsByStatus() {
        Event firstEvent = Event.builder().clientName(CLIENT_NAME).user(USER).contact(CONTACT).scheduledDate(SCHEDULED_DATE).build();
        Event secondEvent = Event.builder().clientName(CLIENT_NAME + "2").user(USER).contact(CONTACT + 2).scheduledDate(SCHEDULED_DATE.plusDays(3)).build();

        when(eventRepository.findAllByUserAndStatus(USER, EventStatus.QUOTE)).thenReturn(List.of(firstEvent, secondEvent));

        List<EventResponseDTO> response = eventService.getAll(false, EventStatus.QUOTE, USER);

        assertEquals(2, response.size());
        assertEquals("clientTest", response.get(0).clientName());
        assertEquals("clientTest2", response.get(1).clientName());
    }

    @Test
    void shouldReturnAllEventsWhereStatusNotCanceled() {
        Event firstEvent = Event.builder().clientName(CLIENT_NAME).user(USER).contact(CONTACT).scheduledDate(SCHEDULED_DATE).build();
        Event secondEvent = Event.builder().clientName(CLIENT_NAME + "2").user(USER).contact(CONTACT + 2).scheduledDate(SCHEDULED_DATE.plusDays(3)).build();
        secondEvent.changeStatus(EventStatus.CANCELED);

        when(eventRepository.findAllByUserAndStatusNot(USER, EventStatus.CANCELED)).thenReturn(List.of(firstEvent));

        List<EventResponseDTO> response = eventService.getAll(true, null, USER);

        assertEquals(1, response.size());
        assertEquals(CLIENT_NAME, response.getFirst().clientName());
    }

    @Test
    void shouldReturnAllEvents() {
        Event firstEvent = Event.builder().clientName(CLIENT_NAME).user(USER).contact(CONTACT).scheduledDate(SCHEDULED_DATE).build();
        Event secondEvent = Event.builder().clientName(CLIENT_NAME + "2").user(USER).contact(CONTACT + 2).scheduledDate(SCHEDULED_DATE.plusDays(3)).build();

        when(eventRepository.findAllByUser(USER)).thenReturn(List.of(firstEvent, secondEvent));

        List<EventResponseDTO> response = eventService.getAll(false, null, USER);

        assertEquals(2, response.size());
        assertEquals(CLIENT_NAME, response.get(0).clientName());
        assertEquals(CLIENT_NAME + 2, response.get(1).clientName());
    }

    @Test
    void shouldUpdateEvent() {
        Event event = Event.builder().clientName(CLIENT_NAME).user(USER).contact(CONTACT).scheduledDate(SCHEDULED_DATE).build();
        EventUpdateDTO request = new EventUpdateDTO("NewName", null, SCHEDULED_DATE.plusYears(2));

        when(eventRepository.findByIdAndUser(1L, USER)).thenReturn(Optional.of(event));

        EventResponseDTO response = eventService.update(1L, request, USER);

        assertEquals("NewName", response.clientName());
        assertEquals(CONTACT, response.contact());
        assertEquals(SCHEDULED_DATE.plusYears(2), response.scheduledDate());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenEventDoesNotExistOnUpdate() {
        EventUpdateDTO request = new EventUpdateDTO("NewName", null, SCHEDULED_DATE.plusYears(2));

        when(eventRepository.findByIdAndUser(1L, USER)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () ->
                eventService.update(1L, request, USER)
        );

        assertEquals("Evento não encontrado.", e.getMessage());
    }
}
