package com.hyunbenny.springsecurity.repository;

import com.hyunbenny.springsecurity.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
