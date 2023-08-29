package com.hyunbenny.springsecurity.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("===== CustomAuthenticationFailureHandler onAuthenticationFailure() called =====");
        String errorMessage = "아이디 혹은 비빌번호가 올바르지 않습니다.";
        setDefaultFailureUrl("/login?error=true&ex=" + errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
