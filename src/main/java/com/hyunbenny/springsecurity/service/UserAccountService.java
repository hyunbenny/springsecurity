package com.hyunbenny.springsecurity.service;

import com.hyunbenny.springsecurity.controller.dto.request.JoinRequest;
import com.hyunbenny.springsecurity.domain.UserAccount;
import com.hyunbenny.springsecurity.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;

    public UserAccount join(JoinRequest request) {
        UserAccount userAccount = UserAccount.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role("ROLE_USER")
                .build();

        UserAccount savedUser = userAccountRepository.save(userAccount);
        log.info(savedUser.toString());
        return savedUser;
    }
}
