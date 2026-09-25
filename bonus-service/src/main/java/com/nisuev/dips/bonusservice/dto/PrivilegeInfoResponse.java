package com.nisuev.dips.bonusservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeInfoResponse {
    private Integer balance;
    private String status;
    private List<BalanceHistory> history;
}
