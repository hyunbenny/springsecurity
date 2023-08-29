package com.hyunbenny.springsecurity.auth.oauth;

import com.hyunbenny.springsecurity.auth.CustomUserDetails;
import com.hyunbenny.springsecurity.domain.UserAccount;
import com.hyunbenny.springsecurity.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuth2UserService extends DefaultOAuth2UserService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String OAUTH2USER_PASSWORD_PREFIX = "hyunbenny_";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("clientRegistration: {}", userRequest.getClientRegistration()); // Oauth 정보 확인(google, kako, naver등)
        log.info("accessToken: {}", userRequest.getAccessToken());

        OAuth2User oauth2User = super.loadUser(userRequest); // 회원 프로필 조회
        log.info("userInfos: {}", oauth2User.getAttributes());

        // 자동 회원가입 진행
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oauth2User.getAttribute("sub");
        String email = oauth2User.getAttribute("email");

        String username = provider + "_" + providerId;

        UserAccount userAccount = userAccountRepository.findByUsername(username).orElse(null);

        if (userAccount == null) {
            userAccount = UserAccount.builder()
                    .username(username)
                    .password(passwordEncoder.encode(OAUTH2USER_PASSWORD_PREFIX + username))
                    .email(email)
                    .role("ROLE_USER")
                    .provider(provider)
                    .providerId(providerId)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();

            userAccountRepository.save(userAccount);
        }

        return new CustomUserDetails(userAccount, oauth2User.getAttributes());
    }
}
