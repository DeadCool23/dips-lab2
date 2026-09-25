package com.nisuev.dips.gatewayservice.repository;

import com.nisuev.dips.gatewayservice.config.ServiceUrlsConfig;
import com.nisuev.dips.gatewayservice.dto.TicketCreateRequest;
import com.nisuev.dips.gatewayservice.dto.TicketInternalResponse;
import com.nisuev.dips.gatewayservice.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TicketRepository {

    private static final String USER_HEADER = "X-User-Name";

    private final RestTemplate restTemplate;
    private final ServiceUrlsConfig urls;

    public List<TicketInternalResponse> findAllByUsername(String username) {
        ResponseEntity<List<TicketInternalResponse>> response = restTemplate.exchange(
                urls.getTicketUrl() + "/api/v1/tickets",
                HttpMethod.GET,
                new HttpEntity<>(headers(username)),
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }

    public TicketInternalResponse findByUidAndUsername(UUID ticketUid, String username) {
        try {
            ResponseEntity<TicketInternalResponse> response = restTemplate.exchange(
                    urls.getTicketUrl() + "/api/v1/tickets/" + ticketUid,
                    HttpMethod.GET,
                    new HttpEntity<>(headers(username)),
                    TicketInternalResponse.class
            );
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("Ticket not found");
        }
    }

    public TicketInternalResponse create(String username, TicketCreateRequest request) {
        ResponseEntity<TicketInternalResponse> response = restTemplate.exchange(
                urls.getTicketUrl() + "/api/v1/tickets",
                HttpMethod.POST,
                new HttpEntity<>(request, jsonHeaders(username)),
                TicketInternalResponse.class
        );
        return response.getBody();
    }

    public void cancel(UUID ticketUid, String username) {
        try {
            restTemplate.exchange(
                    urls.getTicketUrl() + "/api/v1/tickets/" + ticketUid,
                    HttpMethod.DELETE,
                    new HttpEntity<>(headers(username)),
                    Void.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new NotFoundException("Ticket not found");
        }
    }

    private HttpHeaders headers(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(USER_HEADER, username);
        return headers;
    }

    private HttpHeaders jsonHeaders(String username) {
        HttpHeaders headers = headers(username);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
