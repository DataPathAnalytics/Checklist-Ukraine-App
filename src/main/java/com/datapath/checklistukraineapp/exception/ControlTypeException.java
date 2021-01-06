package com.datapath.checklistukraineapp.exception;

public class ControlTypeException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Not found control type. Id %s";

    public ControlTypeException(Integer id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
