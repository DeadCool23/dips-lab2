package com.nisuev.dips.ticketservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private String ticketUid;
    private String flightNumber;
    private Integer price;
    private String status;
}
