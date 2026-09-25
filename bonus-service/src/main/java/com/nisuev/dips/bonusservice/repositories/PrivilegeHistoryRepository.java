package com.nisuev.dips.bonusservice.repositories;

import com.nisuev.dips.bonusservice.entity.PrivilegeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrivilegeHistoryRepository extends JpaRepository<PrivilegeHistory, Integer> {
    List<PrivilegeHistory> findAllByPrivilegeIdOrderByDatetimeDesc(Integer privilegeId);

    Optional<PrivilegeHistory> findByTicketUid(UUID ticketUid);
}
