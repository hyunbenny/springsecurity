package com.hyunbenny.springsecurity.auth;

import com.hyunbenny.springsecurity.domain.UserAccount;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {

    private UserAccount userAccount;
    private Map<String, Object> attributes;

    // 일반 로그인
    public CustomUserDetails(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
    
    // OAuth 로그인
    public CustomUserDetails(UserAccount userAccount, Map<String, Object> attributes) {
        this.userAccount = userAccount;
        this.attributes = attributes;
    }

    /**
     * OAuth2User
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("sub"));
    }

    /**
     * UserDetails
     */
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

    private static List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role));
        return roles;
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" +
                "userAccount=" + userAccount +
                ", attributes=" + attributes +
                '}';
    }
}
