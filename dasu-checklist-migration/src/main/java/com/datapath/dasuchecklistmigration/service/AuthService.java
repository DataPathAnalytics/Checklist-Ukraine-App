package com.datapath.dasuchecklistmigration.service;

import com.datapath.dasuchecklistmigration.dto.request.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class AuthService {

    private final String loginUrl;
    private final String login;
    private final String password;
    private final RestTemplate restTemplate;

    public AuthService(@Value("${checklist.login.url}") String loginUrl,
                       @Value("${checklist.auth.login}") String login,
                       @Value("${checklist.auth.password}") String password,
                       RestTemplate restTemplate) {
        this.loginUrl = loginUrl;
        this.login = login;
        this.password = password;
        this.restTemplate = restTemplate;
    }

    public void login() {
        log.info("Login");
        restTemplate.postForObject(loginUrl, new LoginRequest(login, password), String.class);
    }
}
