package com.nisuev.dips.bonusservice;

import com.nisuev.dips.bonusservice.dto.PrivilegeApplyRequest;
import com.nisuev.dips.bonusservice.dto.PrivilegeApplyResponse;
import com.nisuev.dips.bonusservice.entity.Privilege;
import com.nisuev.dips.bonusservice.repositories.PrivilegeHistoryRepository;
import com.nisuev.dips.bonusservice.repositories.PrivilegeRepository;
import com.nisuev.dips.bonusservice.service.PrivilegeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrivilegeServiceTest {

    @Mock
    private PrivilegeRepository privilegeRepository;

    @Mock
    private PrivilegeHistoryRepository privilegeHistoryRepository;

    @InjectMocks
    private PrivilegeService privilegeService;

    @Test
    void apply_withoutBalance_addsTenPercent() {
        Privilege privilege = new Privilege();
        privilege.setId(1);
        privilege.setUsername("Test Max");
        privilege.setStatus("BRONZE");
        privilege.setBalance(0);
        when(privilegeRepository.findByUsername("Test Max")).thenReturn(Optional.of(privilege));
        when(privilegeRepository.save(any(Privilege.class))).thenAnswer(inv -> inv.getArgument(0));
        when(privilegeHistoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PrivilegeApplyResponse response = privilegeService.apply(
                "Test Max",
                new PrivilegeApplyRequest(UUID.randomUUID().toString(), 1500, false)
        );

        assertThat(response.getPaidByBonuses()).isEqualTo(0);
        assertThat(response.getPaidByMoney()).isEqualTo(1500);
        assertThat(response.getPrivilege().getBalance()).isEqualTo(150);
    }

    @Test
    void apply_fromBalance_debitsAvailable() {
        Privilege privilege = new Privilege();
        privilege.setId(1);
        privilege.setUsername("Test Max");
        privilege.setStatus("GOLD");
        privilege.setBalance(500);
        when(privilegeRepository.findByUsername("Test Max")).thenReturn(Optional.of(privilege));
        when(privilegeRepository.save(any(Privilege.class))).thenAnswer(inv -> inv.getArgument(0));
        when(privilegeHistoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PrivilegeApplyResponse response = privilegeService.apply(
                "Test Max",
                new PrivilegeApplyRequest(UUID.randomUUID().toString(), 1500, true)
        );

        assertThat(response.getPaidByBonuses()).isEqualTo(500);
        assertThat(response.getPaidByMoney()).isEqualTo(1000);
        assertThat(response.getPrivilege().getBalance()).isEqualTo(0);
    }
}
