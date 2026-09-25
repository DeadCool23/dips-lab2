package com.nisuev.dips.gatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeApplyResponse {
    private Integer paidByBonuses;
    private Integer paidByMoney;
    private PrivilegeShortInfo privilege;
}
