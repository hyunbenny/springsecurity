package com.hyunbenny.springsecurity.auth;

import com.hyunbenny.springsecurity.domain.UserAccount;
import com.hyunbenny.springsecurity.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security 설정(SecurityConfig)에서
 * loginProcessingUrl("/login") 설정을 했을 때,
 * `/login` 요청이 오면 자동으로 UserDetailsService의 loadUserByUsername()가 실행된다.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("해당 회원이 존재하지 않습니다."));
        return new CustomUserDetails(userAccount);
    }
}
