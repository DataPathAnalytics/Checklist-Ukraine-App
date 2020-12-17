package com.datapath.checklistukraineapp.util;

public enum UserRole {

    ADMIN("admin"),
    USER("user");

    private String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
