package com.datapath.checklistapp.security;

public class SecurityConstants {

    private SecurityConstants() {
    }

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SECRET_KEY = "introlabanddatapath";

    public static final String USER_IS_DISABLED_ERROR_JSON_MESSAGE = "{\"errorMessageCode\": 2}";
    public static final String BAD_CREDENTIALS_ERROR_JSON_MESSAGE = "{\"errorMessageCode\": 1}";
}
