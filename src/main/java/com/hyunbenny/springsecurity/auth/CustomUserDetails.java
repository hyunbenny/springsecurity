package com.hyunbenny.springsecurity.auth;

import com.hyunbenny.springsecurity.domain.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Spring Security가 `/login` 요청을 낚아채서 로그인 처리 후 세션을 만들어준다.(SecurityContextHolder)
 * SecurityContextHolder에 들어갈 수 있는 객체가 정해져있다. -> `Authentication`
 *
 * Authentication은 User의 정보가 들어있다.
 * User의 정보를 가지고 있는 객체는 UserDetails 타입이어야 한다.
 *
 *
 *
 * ContextHolder - Authentication - UserDetails
 */
public class CustomUserDetails implements UserDetails {

    private UserAccount userAccount;

    public CustomUserDetails(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    private static List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role));
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = getGrantedAuthorities(userAccount.getRole());
        return roles;
    }

    @Override
    public String getPassword() {
        return userAccount.getPassword();
    }

    @Override
    public String getUsername() {
        return userAccount.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
