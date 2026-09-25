package com.nisuev.dips.ticketservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "ticket")
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ticket_uid", nullable = false, unique = true)
    private UUID ticketUid;

    @Column(nullable = false, length = 80)
    private String username;

    @Column(name = "flight_number", nullable = false, length = 20)
    private String flightNumber;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = 20)
    private String status;
}
