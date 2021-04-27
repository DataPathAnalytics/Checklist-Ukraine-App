package com.datapath.analyticapp.exception;

import com.datapath.analyticapp.dao.service.QueryRequest;

public class CypherOperationException extends RuntimeException {

    private static final String MESSAGE = "Node not merged %s";

    public CypherOperationException(QueryRequest request) {
        super(String.format(MESSAGE, request.toString()));
    }

    public CypherOperationException(String message) {
        super(message);
    }
}
