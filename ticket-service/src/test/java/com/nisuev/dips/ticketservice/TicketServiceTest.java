package com.nisuev.dips.ticketservice;

import com.nisuev.dips.ticketservice.dto.TicketCreateRequest;
import com.nisuev.dips.ticketservice.dto.TicketResponse;
import com.nisuev.dips.ticketservice.entity.Ticket;
import com.nisuev.dips.ticketservice.exception.NotFoundException;
import com.nisuev.dips.ticketservice.repositories.TicketRepository;
import com.nisuev.dips.ticketservice.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void create_savesPaidTicket() {
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        TicketResponse response = ticketService.create("Test Max", new TicketCreateRequest("AFL031", 1500));

        assertThat(response.getFlightNumber()).isEqualTo("AFL031");
        assertThat(response.getPrice()).isEqualTo(1500);
        assertThat(response.getStatus()).isEqualTo("PAID");
        assertThat(response.getTicketUid()).isNotBlank();

        ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
        verify(ticketRepository).save(captor.capture());
        assertThat(captor.getValue().getUsername()).isEqualTo("Test Max");
    }

    @Test
    void cancel_marksCanceled() {
        UUID uid = UUID.randomUUID();
        Ticket ticket = new Ticket();
        ticket.setTicketUid(uid);
        ticket.setUsername("Test Max");
        ticket.setFlightNumber("AFL031");
        ticket.setPrice(1500);
        ticket.setStatus("PAID");
        when(ticketRepository.findByTicketUidAndUsername(uid, "Test Max")).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));

        ticketService.cancel(uid, "Test Max");

        assertThat(ticket.getStatus()).isEqualTo("CANCELED");
    }

    @Test
    void getByUid_throwsWhenMissing() {
        UUID uid = UUID.randomUUID();
        when(ticketRepository.findByTicketUidAndUsername(uid, "User")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.getByUidAndUsername(uid, "User"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getAllByUsername_mapsList() {
        Ticket ticket = new Ticket();
        ticket.setTicketUid(UUID.randomUUID());
        ticket.setUsername("User");
        ticket.setFlightNumber("AFL031");
        ticket.setPrice(1500);
        ticket.setStatus("PAID");
        when(ticketRepository.findAllByUsername("User")).thenReturn(List.of(ticket));

        List<TicketResponse> result = ticketService.getAllByUsername("User");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFlightNumber()).isEqualTo("AFL031");
    }
}
