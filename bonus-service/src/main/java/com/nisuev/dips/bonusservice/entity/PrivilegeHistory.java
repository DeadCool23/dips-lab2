package com.nisuev.dips.bonusservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "privilege_history")
@Getter
@Setter
public class PrivilegeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "privilege_id")
    private Integer privilegeId;

    @Column(name = "ticket_uid", nullable = false)
    private UUID ticketUid;

    @Column(nullable = false)
    private LocalDateTime datetime;

    @Column(name = "balance_diff", nullable = false)
    private Integer balanceDiff;

    @Column(name = "operation_type", nullable = false, length = 20)
    private String operationType;
}
