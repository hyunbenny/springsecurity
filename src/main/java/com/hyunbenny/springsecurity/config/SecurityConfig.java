package com.hyunbenny.springsecurity.config;

import com.hyunbenny.springsecurity.auth.CustomLoginFailureHandler;
import com.hyunbenny.springsecurity.auth.oauth.GoogleOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//@EnableWebSecurity // (debug = true) // SpringSecurityFilter(SecurityConfig) 가 Spring Filter Chain에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomLoginFailureHandler customLoginFailureHandler;
    private final GoogleOAuth2UserService googleOAuth2UserService;

// 순환참조 문제로 인해 SecurityConfig에서 빈으로 등록하지 않고 따로 클래스를 만들어 @Component를 이용해서 등록
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/users/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()

                .and()
                .formLogin()
//                .usernameParameter("userId")
                .loginPage("/login")
                .loginProcessingUrl("/login") // `/login`주소가 호출되면 spring security가 낚아채서 대신 로그인을 진행해준다. -> 로그인 처리 로직이 필요없음
                .defaultSuccessUrl("/")
                .failureHandler(customLoginFailureHandler)

                /**
                 * 인증(구글 로그인) 완료 후 처리가 필요하다.
                 * 코드 받기
                 * -> 코드를 통해 엑세스 토큰 받기
                 * -> 사용자 프로필 정보 조회
                 * -> 프로필 정보를 가지고 자동으로 회원가입 처리(정보가 부족한 경우, 회원가입 페이지로 이동하여 직접 회원가입 처리)
                 *
                 * Tip. 구글은 카카오와 달리 코드를 받지 않고 엑세스 토큰 + 프로필 정보를 한번에 받는다.
                 */
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint().userService(googleOAuth2UserService) // 구글 로그인 후 후처리
        ;
    }
}
