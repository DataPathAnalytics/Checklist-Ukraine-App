package com.datapath.checklistukraineapp.exception;

public class ControlObjectException extends RuntimeException {

    private final static String MESSAGE_TEMPLATE = "Control object not found. Id %s";

    public ControlObjectException(String id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
