package com.datapath.analyticapp;

public class Constants {

    public static final String SEARCH_PATTERN = "(?i)^%s.*";

    public static final String AUTHORITY_REPRESENTATIVE_ROLE = "authority_representative";
    public static final String OWNER_ROLE = "owner";
    public static final String SUBJECT_ROLE = "subject";
    public static final String CONTROL_ACTIVITY_ROLE = "control_activity";
    public static final String RESPONSE_SESSION_ROLE = "response_session";
    public static final String FACT_ROLE = "fact";
    public static final String FACT_FEATURE_ROLE = "fact_feature";
    public static final String EVENT_ROLE = "event";

    public final static String DEFAULT_NODE = "Data";
    public final static String DEFAULT_LINK = "HAS_DATA";

    public final static String AUTHORITY_DEFAULT_NODE = "Authority";
    public final static String OWNER_DEFAULT_NODE = "Owner";
    public final static String SUBJECT_DEFAULT_NODE = "Subject";

    public final static String FACT_DEFAULT_NODE = "Fact";
    public final static String FACT_DEFAULT_LINK = "CREATED_FACT";

    public final static String EVENT_DEFAULT_NODE = "Event";
    public final static String EVENT_DEFAULT_LINK = "HAS_EVENT";

    public final static String FACT_QUESTION_LINK = "IS_FACT_OF";
    public final static String SESSION_QUESTION_LINK = "HAS_QUESTION";
    public final static String SUBJECT_QUESTION_LINK = "WAS_CHECKED_BY";
}
