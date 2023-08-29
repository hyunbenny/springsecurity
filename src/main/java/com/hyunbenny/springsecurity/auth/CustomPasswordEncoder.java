package com.hyunbenny.springsecurity.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder extends BCryptPasswordEncoder {
}
