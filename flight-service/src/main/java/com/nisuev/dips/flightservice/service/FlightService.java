package com.nisuev.dips.flightservice.service;

import com.nisuev.dips.flightservice.dto.FlightResponse;
import com.nisuev.dips.flightservice.dto.PaginationResponse;
import com.nisuev.dips.flightservice.entity.Flight;
import com.nisuev.dips.flightservice.exception.NotFoundException;
import com.nisuev.dips.flightservice.repositories.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final FlightRepository flightRepository;

    public PaginationResponse getFlights(int page, int size) {
        int pageIndex = Math.max(page - 1, 0);
        Page<Flight> result = flightRepository.findAll(PageRequest.of(pageIndex, size));
        List<FlightResponse> items = result.getContent().stream().map(this::toDto).toList();
        return new PaginationResponse(page, size, result.getTotalElements(), items);
    }

    public FlightResponse getByFlightNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Flight not found"));
    }

    private FlightResponse toDto(Flight flight) {
        return new FlightResponse(
                flight.getFlightNumber(),
                flight.getFromAirport().getCity() + " " + flight.getFromAirport().getName(),
                flight.getToAirport().getCity() + " " + flight.getToAirport().getName(),
                flight.getDatetime().toLocalDateTime().format(DATE_FMT),
                flight.getPrice()
        );
    }
}
