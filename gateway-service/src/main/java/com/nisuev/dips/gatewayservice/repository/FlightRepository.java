package com.nisuev.dips.gatewayservice.repository;

import com.nisuev.dips.gatewayservice.config.ServiceUrlsConfig;
import com.nisuev.dips.gatewayservice.dto.FlightResponse;
import com.nisuev.dips.gatewayservice.dto.PaginationResponse;
import com.nisuev.dips.gatewayservice.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Repository
@RequiredArgsConstructor
public class FlightRepository {

    private final RestTemplate restTemplate;
    private final ServiceUrlsConfig urls;

    public PaginationResponse findAll(int page, int size) {
        return restTemplate.getForObject(
                urls.getFlightUrl() + "/api/v1/flights?page={page}&size={size}",
                PaginationResponse.class,
                page, size
        );
    }

    public FlightResponse findByFlightNumber(String flightNumber) {
        try {
            return restTemplate.getForObject(
                    urls.getFlightUrl() + "/api/v1/flights/{flightNumber}",
                    FlightResponse.class,
                    flightNumber
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("Flight not found");
        }
    }
}
