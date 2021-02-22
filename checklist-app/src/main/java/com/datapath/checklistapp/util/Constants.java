package com.datapath.checklistapp.util;

public final class Constants {
    private Constants() {
    }

    private static final String LOGIN_URL = "/login";
    private static final String REGISTER_URL = "/users/register";
    private static final String PUBLIC_MAPPING_URL = "/mappings/public";

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

    public static final Integer ACTIVITY_QUESTION_TYPE = 1;
    public static final Integer SESSION_QUESTION_TYPE = 2;
    public static final Integer FACT_QUESTION_TYPE = 3;
    public static final Integer OBJECT_QUESTION_TYPE = 4;
    public static final Integer FEATURE_QUESTION_TYPE = 5;
    public static final Integer AUTHORITY_QUESTION_TYPE = 6;

    public final static String DEFAULT_EVENT_CHECKLIST_COUNT_STR = "5";
    public final static String DEFAULT_EVENT_CHECKLIST_PAGE_STR = "0";
    public final static Integer DEFAULT_EVENT_CHECKLIST_COUNT = Integer.parseInt(DEFAULT_EVENT_CHECKLIST_COUNT_STR);
    public final static Integer DEFAULT_EVENT_CHECKLIST_PAGE = Integer.parseInt(DEFAULT_EVENT_CHECKLIST_PAGE_STR);

    public static final String SEARCH_PATTERN = "(?i)^%s.*";

    public static final String[] PUBLIC_ENDPOINTS = {
            LOGIN_URL,
            REGISTER_URL,
            PUBLIC_MAPPING_URL,
            RESET_PASSWORD_MAIL_URL,
            RESET_PASSWORD_CHECK_URL,
            RESET_PASSWORD_SAVE_URL
    };
}
