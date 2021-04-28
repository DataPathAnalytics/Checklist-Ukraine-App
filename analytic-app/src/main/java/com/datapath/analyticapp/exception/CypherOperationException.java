package com.datapath.analyticapp.exception;

public class CypherOperationException extends RuntimeException {

    private static final String MESSAGE = "Node not merged. ";

    public CypherOperationException(String request) {
        super(MESSAGE + request);
    }

    public CypherOperationException() {
        super(MESSAGE);
    }
}
