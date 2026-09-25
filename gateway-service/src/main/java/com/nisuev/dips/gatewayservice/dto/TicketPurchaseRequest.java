package com.nisuev.dips.gatewayservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TicketPurchaseRequest")
public class TicketPurchaseRequest {

    @NotBlank
    private String flightNumber;

    @NotNull
    @Positive
    private Integer price;

    @NotNull
    private Boolean paidFromBalance;
}
