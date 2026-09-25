package com.nisuev.dips.gatewayservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ServiceUrlsConfig {

    private final String flightUrl;
    private final String ticketUrl;
    private final String bonusUrl;

    public ServiceUrlsConfig(
            @Value("${services.flight-url}") String flightUrl,
            @Value("${services.ticket-url}") String ticketUrl,
            @Value("${services.bonus-url}") String bonusUrl) {
        this.flightUrl = flightUrl;
        this.ticketUrl = ticketUrl;
        this.bonusUrl = bonusUrl;
    }
}
