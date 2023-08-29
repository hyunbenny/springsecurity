package com.hyunbenny.springsecurity.controller;

import com.hyunbenny.springsecurity.controller.dto.request.JoinRequest;
import com.hyunbenny.springsecurity.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
