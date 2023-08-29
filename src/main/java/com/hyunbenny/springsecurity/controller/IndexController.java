package com.hyunbenny.springsecurity.controller;

import com.hyunbenny.springsecurity.auth.CustomUserDetails;
import com.hyunbenny.springsecurity.controller.dto.request.JoinRequest;
import com.hyunbenny.springsecurity.domain.UserAccount;
import com.hyunbenny.springsecurity.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserAccountService userAccountService;

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/users")
    public String user() {
        return "user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "ex", required = false) String exception,
                            Model model) {

        log.info("error : {}, exception : {}", error, exception);

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "loginForm";
    }

    @GetMapping("/test/login")
    @ResponseBody
    public Authentication testLogin(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("{}", customUserDetails);
        return authentication;
    }

    /**
     * `Authentication`은 `UserDetails`와 `OAuth2User` 2가지 타입의 객체를 가질 수 있다.
     *
     *  Authentication 객체를 받아서 형변환해서 사용하거나
     * `@AuthenticationPrincipal`을 사용할 수 있다.(@AuthenticationPrincipal UserDetails userDetails)
     *
     * 근데 UserDetails 따로, OAuth2User 따로 컨트롤러에 만드려면 복잡하다.. 
     * 그러니깐 CustomUserDetails에 추가로 OAuth2User를 구현하자
     */
    @GetMapping("/test/oauth/login")
    @ResponseBody
    public UserAccount testOAuthLogin(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        // OAuth 로그인은 UserDatails로 캐스팅을 할 수 없다. -> OAuth2User로 받아야 함
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("{}", customUserDetails.getUserAccount());
        return customUserDetails.getUserAccount();
    }

    @GetMapping("/join")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(JoinRequest request) {
        log.info("{}", request.toString());
        userAccountService.join(request);
        return "redirect:/login";
    }

    @Secured("ROLE_ADMIN") // AND, OR 조건 불가능 (@EnableGlobalMethodSecurity(securedEnabled = true) 옵션 활성화 해야 사용가능
    @GetMapping("/admin-info")
    @ResponseBody
    public String info() {
        return "admin info";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') OR hasRole('ROLE_ADMIN')") // @EnableGlobalMethodSecurity(prePostEnabled = true) 옵션 활성화 해야 사용가능
    @GetMapping("/manager-info")
    @ResponseBody
    public String adminInfo() {
        return "manager info";
    }

}
