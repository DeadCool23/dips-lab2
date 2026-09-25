package com.nisuev.dips.gatewayservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FlightResponse")
public class FlightResponse {
    private String flightNumber;
    private String fromAirport;
    private String toAirport;
    private String date;
    private Integer price;
}
