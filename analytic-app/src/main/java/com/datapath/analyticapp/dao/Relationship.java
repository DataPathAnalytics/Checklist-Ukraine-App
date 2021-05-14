package com.datapath.analyticapp.dao;

public final class Relationship {

    private Relationship() {
    }

    public static final String HAS_AUTHOR = "HAS_AUTHOR";
    public static final String HAS_MEMBER = "HAS_MEMBER";
    public static final String HAS_REVIEWER = "HAS_REVIEWER";
    public static final String HAS_RESPONSE_SESSION = "HAS_RESPONSE_SESSION";
    public static final String IN_KNOWLEDGE_CLASS = "IN_KNOWLEDGE_CLASS";

    public final static String FACT_DEFAULT_LINK = "HAS_FACT";
    public final static String EVENT_DEFAULT_LINK = "HAS_EVENT";

    public final static String FACT_QUESTION_LINK = "IS_FACT_OF";
    public final static String SESSION_QUESTION_LINK = "HAS_QUESTION";
    public final static String PARENT_QUESTION_LINK = "WAS_CHECKED_BY";
}
