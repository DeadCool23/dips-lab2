package com.nisuev.dips.bonusservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeShortInfo {
    private Integer balance;
    private String status;
}
