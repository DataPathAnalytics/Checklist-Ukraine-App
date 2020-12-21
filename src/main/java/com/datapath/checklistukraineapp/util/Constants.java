package com.datapath.checklistukraineapp.util;

public final class Constants {
    private Constants() {
    }

    private static final String LOGIN_URL = "/login";
    private static final String REGISTER_URL = "/users/register";
    private static final String PUBLIC_MAPPING_URL = "/mapping/public";

    public static final String USER_REGISTRATION_MESSAGE_SUBJECT = "User registration";
    public static final String USER_REGISTRATION_MESSAGE_TEMPLATE = "New user registered. %s %s %s";
    public static final String ACCOUNT_VERIFICATION_MESSAGE_SUBJECT = "Account verification";
    public static final String RESET_PASSWORD_MESSAGE_SUBJECT = "Reset password";
    public static final String RESET_PASSWORD_MESSAGE_TEMPLATE = "%s/reset-password?token=%s";

    private static final String RESET_PASSWORD_MAIL_URL = "/users/password/reset/mail";
    private static final String RESET_PASSWORD_CHECK_URL = "/users/password/reset/check";
    private static final String RESET_PASSWORD_SAVE_URL = "/users/password/reset/save";

    public static final String[] PUBLIC_ENDPOINTS = {
            LOGIN_URL,
            REGISTER_URL,
            PUBLIC_MAPPING_URL,
            RESET_PASSWORD_MAIL_URL,
            RESET_PASSWORD_CHECK_URL,
            RESET_PASSWORD_SAVE_URL
    };
}
