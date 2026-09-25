package com.nisuev.dips.gatewayservice;

import com.nisuev.dips.gatewayservice.dto.*;
import com.nisuev.dips.gatewayservice.exception.BadRequestException;
import com.nisuev.dips.gatewayservice.exception.NotFoundException;
import com.nisuev.dips.gatewayservice.repository.BonusRepository;
import com.nisuev.dips.gatewayservice.repository.FlightRepository;
import com.nisuev.dips.gatewayservice.repository.TicketRepository;
import com.nisuev.dips.gatewayservice.service.GatewayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GatewayServiceTest {

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private BonusRepository bonusRepository;

    @InjectMocks
    private GatewayService gatewayService;

    @Test
    void purchase_orchestratesFlightTicketAndBonus() {
        FlightResponse flight = new FlightResponse(
                "AFL031", "Санкт-Петербург Пулково", "Москва Шереметьево", "2021-10-08 20:00", 1500);
        when(flightRepository.findByFlightNumber("AFL031")).thenReturn(flight);

        TicketInternalResponse ticket = new TicketInternalResponse(
                UUID.randomUUID().toString(), "AFL031", 1500, "PAID");
        when(ticketRepository.create(eq("Test Max"), any(TicketCreateRequest.class))).thenReturn(ticket);

        PrivilegeApplyResponse bonus = new PrivilegeApplyResponse(
                0, 1500, new PrivilegeShortInfo(150, "BRONZE"));
        when(bonusRepository.apply(eq("Test Max"), any(PrivilegeApplyRequest.class))).thenReturn(bonus);

        TicketPurchaseResponse response = gatewayService.purchase(
                "Test Max",
                new TicketPurchaseRequest("AFL031", 1500, false)
        );

        assertThat(response.getFlightNumber()).isEqualTo("AFL031");
        assertThat(response.getFromAirport()).isEqualTo("Санкт-Петербург Пулково");
        assertThat(response.getPaidByMoney()).isEqualTo(1500);
        assertThat(response.getPaidByBonuses()).isEqualTo(0);
        assertThat(response.getPrivilege().getBalance()).isEqualTo(150);
    }

    @Test
    void purchase_throwsBadRequestWhenFlightMissing() {
        when(flightRepository.findByFlightNumber("XXX")).thenThrow(new NotFoundException("Flight not found"));

        assertThatThrownBy(() -> gatewayService.purchase(
                "User", new TicketPurchaseRequest("XXX", 100, false)))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void cancelTicket_callsTicketAndBonus() {
        UUID uid = UUID.randomUUID();

        gatewayService.cancelTicket(uid, "Test Max");

        verify(ticketRepository).cancel(uid, "Test Max");
        verify(bonusRepository).rollback("Test Max", uid);
    }

    @Test
    void getTickets_enrichesWithFlightData() {
        TicketInternalResponse ticket = new TicketInternalResponse(
                "uid-1", "AFL031", 1500, "PAID");
        when(ticketRepository.findAllByUsername("User")).thenReturn(List.of(ticket));
        when(flightRepository.findByFlightNumber("AFL031")).thenReturn(
                new FlightResponse("AFL031", "Санкт-Петербург Пулково", "Москва Шереметьево",
                        "2021-10-08 20:00", 1500));

        List<TicketResponse> result = gatewayService.getTickets("User");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFromAirport()).isEqualTo("Санкт-Петербург Пулково");
        assertThat(result.get(0).getDate()).isEqualTo("2021-10-08 20:00");
    }
}
