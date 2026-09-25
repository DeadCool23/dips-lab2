package com.nisuev.dips.bonusservice.service;

import com.nisuev.dips.bonusservice.dto.*;
import com.nisuev.dips.bonusservice.entity.Privilege;
import com.nisuev.dips.bonusservice.entity.PrivilegeHistory;
import com.nisuev.dips.bonusservice.exception.NotFoundException;
import com.nisuev.dips.bonusservice.repositories.PrivilegeHistoryRepository;
import com.nisuev.dips.bonusservice.repositories.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeHistoryRepository privilegeHistoryRepository;

    public PrivilegeInfoResponse getInfo(String username) {
        Privilege privilege = getOrCreate(username);
        List<BalanceHistory> history = privilegeHistoryRepository
                .findAllByPrivilegeIdOrderByDatetimeDesc(privilege.getId())
                .stream()
                .map(this::toHistoryDto)
                .toList();
        return new PrivilegeInfoResponse(privilege.getBalance(), privilege.getStatus(), history);
    }

    @Transactional
    public PrivilegeApplyResponse apply(String username, PrivilegeApplyRequest request) {
        Privilege privilege = getOrCreate(username);
        int price = request.getPrice();
        boolean paidFromBalance = Boolean.TRUE.equals(request.getPaidFromBalance());
        UUID ticketUid = UUID.fromString(request.getTicketUid());

        int paidByBonuses;
        int paidByMoney;
        if (paidFromBalance) {
            paidByBonuses = Math.min(privilege.getBalance(), price);
            paidByMoney = price - paidByBonuses;
            privilege.setBalance(privilege.getBalance() - paidByBonuses);
            if (paidByBonuses > 0) {
                saveHistory(privilege.getId(), ticketUid, paidByBonuses, "DEBIT_THE_ACCOUNT");
            }
        } else {
            paidByBonuses = 0;
            paidByMoney = price;
            int bonus = price / 10;
            privilege.setBalance(privilege.getBalance() + bonus);
            saveHistory(privilege.getId(), ticketUid, bonus, "FILL_IN_BALANCE");
        }
        privilegeRepository.save(privilege);

        return new PrivilegeApplyResponse(
                paidByBonuses,
                paidByMoney,
                new PrivilegeShortInfo(privilege.getBalance(), privilege.getStatus())
        );
    }

    @Transactional
    public void rollback(String username, UUID ticketUid) {
        Privilege privilege = getOrCreate(username);
        PrivilegeHistory history = privilegeHistoryRepository.findByTicketUid(ticketUid)
                .orElseThrow(() -> new NotFoundException("History not found"));

        if ("FILL_IN_BALANCE".equals(history.getOperationType())) {
            privilege.setBalance(Math.max(0, privilege.getBalance() - history.getBalanceDiff()));
        } else if ("DEBIT_THE_ACCOUNT".equals(history.getOperationType())) {
            privilege.setBalance(privilege.getBalance() + history.getBalanceDiff());
        }
        privilegeRepository.save(privilege);
    }

    private Privilege getOrCreate(String username) {
        return privilegeRepository.findByUsername(username).orElseGet(() -> {
            Privilege privilege = new Privilege();
            privilege.setUsername(username);
            privilege.setStatus("BRONZE");
            privilege.setBalance(0);
            return privilegeRepository.save(privilege);
        });
    }

    private void saveHistory(Integer privilegeId, UUID ticketUid, int diff, String type) {
        PrivilegeHistory history = new PrivilegeHistory();
        history.setPrivilegeId(privilegeId);
        history.setTicketUid(ticketUid);
        history.setDatetime(LocalDateTime.now(ZoneOffset.UTC));
        history.setBalanceDiff(diff);
        history.setOperationType(type);
        privilegeHistoryRepository.save(history);
    }

    private BalanceHistory toHistoryDto(PrivilegeHistory history) {
        return new BalanceHistory(
                history.getDatetime().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                history.getTicketUid().toString(),
                history.getBalanceDiff(),
                history.getOperationType()
        );
    }
}
