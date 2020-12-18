package com.datapath.checklistukraineapp.util;

public enum UserRole {

    admin("admin", "адмін"),
    auditor("auditor", "аудитор"),
    methodologist("methodologist", "методолог");

    private String value;
    private String translate;

    UserRole(String value, String translate) {
        this.value = value;
        this.translate = translate;
    }

    public String getValue() {
        return value;
    }

    public String getTranslate() {
        return translate;
    }
}
