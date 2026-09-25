package com.nisuev.dips.flightservice;

import com.nisuev.dips.flightservice.dto.FlightResponse;
import com.nisuev.dips.flightservice.dto.PaginationResponse;
import com.nisuev.dips.flightservice.entity.Airport;
import com.nisuev.dips.flightservice.entity.Flight;
import com.nisuev.dips.flightservice.exception.NotFoundException;
import com.nisuev.dips.flightservice.repositories.FlightRepository;
import com.nisuev.dips.flightservice.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    private Flight sampleFlight;

    @BeforeEach
    void setUp() {
        Airport from = new Airport();
        from.setCity("Санкт-Петербург");
        from.setName("Пулково");
        Airport to = new Airport();
        to.setCity("Москва");
        to.setName("Шереметьево");

        sampleFlight = new Flight();
        sampleFlight.setFlightNumber("AFL031");
        sampleFlight.setDatetime(OffsetDateTime.of(2021, 10, 8, 20, 0, 0, 0, ZoneOffset.UTC));
        sampleFlight.setFromAirport(from);
        sampleFlight.setToAirport(to);
        sampleFlight.setPrice(1500);
    }

    @Test
    void getByFlightNumber_returnsDto() {
        when(flightRepository.findByFlightNumber("AFL031")).thenReturn(Optional.of(sampleFlight));

        FlightResponse response = flightService.getByFlightNumber("AFL031");

        assertThat(response.getFlightNumber()).isEqualTo("AFL031");
        assertThat(response.getFromAirport()).isEqualTo("Санкт-Петербург Пулково");
        assertThat(response.getToAirport()).isEqualTo("Москва Шереметьево");
        assertThat(response.getDate()).isEqualTo("2021-10-08 20:00");
        assertThat(response.getPrice()).isEqualTo(1500);
    }

    @Test
    void getByFlightNumber_throwsWhenMissing() {
        when(flightRepository.findByFlightNumber("XXX")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> flightService.getByFlightNumber("XXX"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getFlights_returnsPage() {
        when(flightRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(sampleFlight), PageRequest.of(0, 10), 1));

        PaginationResponse response = flightService.getFlights(1, 10);

        assertThat(response.getPage()).isEqualTo(1);
        assertThat(response.getPageSize()).isEqualTo(10);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getItems()).hasSize(1);
    }
}
