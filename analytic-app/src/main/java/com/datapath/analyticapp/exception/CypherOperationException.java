package com.datapath.analyticapp.exception;

public class CypherOperationException extends RuntimeException {

    private static final String MERGE_ERROR = "Node not merged. ";
    public static final String NODE_NOT_PROCESSED = "Can't process node %s";

    public CypherOperationException(String message, String param) {
        super(String.format(message, param));
    }

    public CypherOperationException(String request) {
        super(MERGE_ERROR + request);
    }

    public CypherOperationException() {
        super(MERGE_ERROR);
    }
}
