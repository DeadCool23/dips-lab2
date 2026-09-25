package com.nisuev.dips.flightservice.controllers;

import com.nisuev.dips.flightservice.dto.ErrorResponse;
import com.nisuev.dips.flightservice.dto.FlightResponse;
import com.nisuev.dips.flightservice.dto.PaginationResponse;
import com.nisuev.dips.flightservice.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
@Tag(name = "Flight API")
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    @Operation(summary = "Получить список рейсов")
    @ApiResponse(responseCode = "200", description = "Список рейсов",
            content = @Content(schema = @Schema(implementation = PaginationResponse.class)))
    public ResponseEntity<PaginationResponse> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(flightService.getFlights(page, size));
    }

    @GetMapping("/{flightNumber}")
    @Operation(summary = "Получить рейс по номеру")
    @ApiResponse(responseCode = "200", description = "Рейс найден",
            content = @Content(schema = @Schema(implementation = FlightResponse.class)))
    @ApiResponse(responseCode = "404", description = "Рейс не найден",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<FlightResponse> getByNumber(@PathVariable String flightNumber) {
        return ResponseEntity.ok(flightService.getByFlightNumber(flightNumber));
    }
}
