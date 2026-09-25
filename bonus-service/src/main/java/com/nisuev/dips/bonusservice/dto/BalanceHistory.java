package com.nisuev.dips.bonusservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceHistory {
    private String date;
    private String ticketUid;
    private Integer balanceDiff;
    private String operationType;
}
