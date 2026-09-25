package com.nisuev.dips.gatewayservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "BalanceHistory")
public class BalanceHistory {
    private String date;
    private String ticketUid;
    private Integer balanceDiff;
    private String operationType;
}
