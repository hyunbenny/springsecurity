package com.hyunbenny.springsecurity.controller;

import com.hyunbenny.springsecurity.controller.dto.request.JoinRequest;
import com.hyunbenny.springsecurity.controller.dto.request.LoginRequest;
import com.hyunbenny.springsecurity.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String loginForm() {
        return "loginForm";
    }

    @PostMapping("/login")
    public String login(LoginRequest request) {
        log.info("{}", request);
        return "redirect:/";
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
}
