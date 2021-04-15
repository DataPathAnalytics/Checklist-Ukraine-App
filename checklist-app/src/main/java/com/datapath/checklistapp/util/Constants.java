package com.datapath.checklistapp.util;

public final class Constants {
    private Constants() {
    }

    private static final String LOGIN_URL = "/login";
    private static final String REGISTER_URL = "/users/register";
    private static final String EXPORT_MAPPING_URL = "/export/*";

    public static final String TOOL_NAME = "Ukraine checklist tool";
    public static final String USER_REGISTRATION_MESSAGE_SUBJECT = "User registration";
    public static final String USER_REGISTRATION_MESSAGE_TEMPLATE = "New user registered. %s %s %s";
    public static final String ACCOUNT_VERIFICATION_MESSAGE_SUBJECT = "Account verification";
    public static final String RESET_PASSWORD_MESSAGE_SUBJECT = "Reset password";
    public static final String RESET_PASSWORD_MESSAGE_TEMPLATE = "%s/reset-password?token=%s";

    private static final String RESET_PASSWORD_MAIL_URL = "/users/password/reset/mail";
    private static final String RESET_PASSWORD_CHECK_URL = "/users/password/reset/check";
    private static final String RESET_PASSWORD_SAVE_URL = "/users/password/reset/save";

    public static final String ADMIN_ROLE = "admin";
    public static final String AUDITOR_ROLE = "auditor";
    public static final String METHODOLOGIST_ROLE = "methodologist";

    public static final String UNGROUPED_NAME = "ungrouped";

    public static final Integer IN_PROCESS_STATUS = 1;
    public static final Integer IN_COMPLETED_STATUS = 2;

    public static final Integer ACTIVITY_TEMPLATE_TYPE = 1;
    public static final Integer SESSION_TEMPLATE_TYPE = 2;

    public final static String DEFAULT_EVENT_CHECKLIST_COUNT_STR = "5";
    public final static String DEFAULT_EVENT_CHECKLIST_PAGE_STR = "0";
    public final static Integer DEFAULT_EVENT_CHECKLIST_COUNT = Integer.parseInt(DEFAULT_EVENT_CHECKLIST_COUNT_STR);
    public final static Integer DEFAULT_EVENT_CHECKLIST_PAGE = Integer.parseInt(DEFAULT_EVENT_CHECKLIST_PAGE_STR);

    public static final String SEARCH_PATTERN = "(?i)^%s.*";

    public static final String[] PUBLIC_ENDPOINTS = {
            LOGIN_URL,
            REGISTER_URL,
            RESET_PASSWORD_MAIL_URL,
            RESET_PASSWORD_CHECK_URL,
            RESET_PASSWORD_SAVE_URL,
            EXPORT_MAPPING_URL
    };
}
