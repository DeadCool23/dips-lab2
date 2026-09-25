package com.nisuev.dips.flightservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Страница списка рейсов")
public class PaginationResponse {
    private Integer page;
    private Integer pageSize;
    private Long totalElements;
    private List<FlightResponse> items;
}
