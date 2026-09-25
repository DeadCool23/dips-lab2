package com.nisuev.dips.gatewayservice.service;

import com.nisuev.dips.gatewayservice.dto.*;
import com.nisuev.dips.gatewayservice.exception.BadRequestException;
import com.nisuev.dips.gatewayservice.exception.NotFoundException;
import com.nisuev.dips.gatewayservice.repository.BonusRepository;
import com.nisuev.dips.gatewayservice.repository.FlightRepository;
import com.nisuev.dips.gatewayservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GatewayService {

    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;
    private final BonusRepository bonusRepository;

    public PaginationResponse getFlights(int page, int size) {
        return flightRepository.findAll(page, size);
    }

    public PrivilegeInfoResponse getPrivilege(String username) {
        return bonusRepository.getPrivilege(username);
    }

    public List<TicketResponse> getTickets(String username) {
        return ticketRepository.findAllByUsername(username).stream()
                .map(this::enrich)
                .toList();
    }

    public TicketResponse getTicket(UUID ticketUid, String username) {
        return enrich(ticketRepository.findByUidAndUsername(ticketUid, username));
    }

    public TicketPurchaseResponse purchase(String username, TicketPurchaseRequest request) {
        FlightResponse flight;
        try {
            flight = flightRepository.findByFlightNumber(request.getFlightNumber());
        } catch (NotFoundException e) {
            throw new BadRequestException("Flight not found");
        }

        TicketInternalResponse ticket = ticketRepository.create(
                username,
                new TicketCreateRequest(request.getFlightNumber(), request.getPrice())
        );

        PrivilegeApplyResponse bonus = bonusRepository.apply(
                username,
                new PrivilegeApplyRequest(ticket.getTicketUid(), request.getPrice(), request.getPaidFromBalance())
        );

        return new TicketPurchaseResponse(
                ticket.getTicketUid(),
                request.getFlightNumber(),
                flight.getFromAirport(),
                flight.getToAirport(),
                flight.getDate(),
                request.getPrice(),
                bonus.getPaidByMoney(),
                bonus.getPaidByBonuses(),
                ticket.getStatus(),
                bonus.getPrivilege()
        );
    }

    public void cancelTicket(UUID ticketUid, String username) {
        ticketRepository.cancel(ticketUid, username);
        bonusRepository.rollback(username, ticketUid);
    }

    public UserInfoResponse getUserInfo(String username) {
        List<TicketResponse> tickets = getTickets(username);
        PrivilegeInfoResponse privilegeInfo = bonusRepository.getPrivilege(username);
        PrivilegeShortInfo privilege = new PrivilegeShortInfo(privilegeInfo.getBalance(), privilegeInfo.getStatus());
        return new UserInfoResponse(tickets, privilege);
    }

    private TicketResponse enrich(TicketInternalResponse ticket) {
        FlightResponse flight = flightRepository.findByFlightNumber(ticket.getFlightNumber());
        return new TicketResponse(
                ticket.getTicketUid(),
                ticket.getFlightNumber(),
                flight.getFromAirport(),
                flight.getToAirport(),
                flight.getDate(),
                ticket.getPrice(),
                ticket.getStatus()
        );
    }
}
