package com.nisuev.dips.ticketservice.service;

import com.nisuev.dips.ticketservice.dto.TicketCreateRequest;
import com.nisuev.dips.ticketservice.dto.TicketResponse;
import com.nisuev.dips.ticketservice.entity.Ticket;
import com.nisuev.dips.ticketservice.exception.NotFoundException;
import com.nisuev.dips.ticketservice.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public List<TicketResponse> getAllByUsername(String username) {
        return ticketRepository.findAllByUsername(username).stream().map(this::toDto).toList();
    }

    public TicketResponse getByUidAndUsername(UUID ticketUid, String username) {
        return ticketRepository.findByTicketUidAndUsername(ticketUid, username)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));
    }

    @Transactional
    public TicketResponse create(String username, TicketCreateRequest request) {
        Ticket ticket = new Ticket();
        ticket.setTicketUid(UUID.randomUUID());
        ticket.setUsername(username);
        ticket.setFlightNumber(request.getFlightNumber());
        ticket.setPrice(request.getPrice());
        ticket.setStatus("PAID");
        return toDto(ticketRepository.save(ticket));
    }

    @Transactional
    public void cancel(UUID ticketUid, String username) {
        Ticket ticket = ticketRepository.findByTicketUidAndUsername(ticketUid, username)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));
        ticket.setStatus("CANCELED");
        ticketRepository.save(ticket);
    }

    private TicketResponse toDto(Ticket ticket) {
        return new TicketResponse(
                ticket.getTicketUid().toString(),
                ticket.getFlightNumber(),
                ticket.getPrice(),
                ticket.getStatus()
        );
    }
}
