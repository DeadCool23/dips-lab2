package com.nisuev.dips.gatewayservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PaginationResponse")
public class PaginationResponse {
    private Integer page;
    private Integer pageSize;
    private Long totalElements;
    private List<FlightResponse> items;
}
