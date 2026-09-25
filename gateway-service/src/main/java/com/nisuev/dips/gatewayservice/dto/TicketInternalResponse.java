package com.nisuev.dips.gatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Internal DTO from ticket-service (without flight enrichment). */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketInternalResponse {
    private String ticketUid;
    private String flightNumber;
    private Integer price;
    private String status;
}
