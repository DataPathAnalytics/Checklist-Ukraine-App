package com.datapath.checklistukraineapp.exception;

public class ControlStatusException extends RuntimeException {

    private final static String MESSAGE_TEMPLATE = "Control status not found. Id %s";

    public ControlStatusException(Integer id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
