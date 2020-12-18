package com.datapath.checklistukraineapp.util;

public final class Constants {
    private Constants() {
    }

    private static final String LOGIN_URL = "/login";
    private static final String REGISTER_URL = "/users/register";
    private static final String PUBLIC_MAPPING_URL = "/mapping/public";

    public static final String[] PUBLIC_ENDPOINTS = {
            LOGIN_URL, REGISTER_URL, PUBLIC_MAPPING_URL
    };
}
