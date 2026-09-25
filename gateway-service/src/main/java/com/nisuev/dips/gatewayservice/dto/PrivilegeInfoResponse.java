package com.nisuev.dips.gatewayservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PrivilegeInfoResponse")
public class PrivilegeInfoResponse {
    private Integer balance;
    private String status;
    private List<BalanceHistory> history;
}
