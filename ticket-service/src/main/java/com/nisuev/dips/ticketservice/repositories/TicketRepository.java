package com.nisuev.dips.ticketservice.repositories;

import com.nisuev.dips.ticketservice.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findAllByUsername(String username);

    Optional<Ticket> findByTicketUidAndUsername(UUID ticketUid, String username);
}
