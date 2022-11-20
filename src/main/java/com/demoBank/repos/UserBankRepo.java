package com.demoBank.repos;

import com.demoBank.domain.UserBank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBankRepo extends JpaRepository<UserBank, Long> {
    UserBank findByUsername(String username);
}
