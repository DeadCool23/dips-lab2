package com.nisuev.dips.gatewayservice.repository;

import com.nisuev.dips.gatewayservice.config.ServiceUrlsConfig;
import com.nisuev.dips.gatewayservice.dto.PrivilegeApplyRequest;
import com.nisuev.dips.gatewayservice.dto.PrivilegeApplyResponse;
import com.nisuev.dips.gatewayservice.dto.PrivilegeInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BonusRepository {

    private static final String USER_HEADER = "X-User-Name";

    private final RestTemplate restTemplate;
    private final ServiceUrlsConfig urls;

    public PrivilegeInfoResponse getPrivilege(String username) {
        ResponseEntity<PrivilegeInfoResponse> response = restTemplate.exchange(
                urls.getBonusUrl() + "/api/v1/privilege",
                HttpMethod.GET,
                new HttpEntity<>(headers(username)),
                PrivilegeInfoResponse.class
        );
        return response.getBody();
    }

    public PrivilegeApplyResponse apply(String username, PrivilegeApplyRequest request) {
        ResponseEntity<PrivilegeApplyResponse> response = restTemplate.exchange(
                urls.getBonusUrl() + "/api/v1/privilege",
                HttpMethod.POST,
                new HttpEntity<>(request, jsonHeaders(username)),
                PrivilegeApplyResponse.class
        );
        return response.getBody();
    }

    public void rollback(String username, UUID ticketUid) {
        try {
            restTemplate.exchange(
                    urls.getBonusUrl() + "/api/v1/privilege?ticketUid=" + ticketUid,
                    HttpMethod.DELETE,
                    new HttpEntity<>(headers(username)),
                    Void.class
            );
        } catch (HttpClientErrorException.NotFound ignored) {
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
