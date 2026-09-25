package com.nisuev.dips.bonusservice.repositories;

import com.nisuev.dips.bonusservice.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
    Optional<Privilege> findByUsername(String username);
}
