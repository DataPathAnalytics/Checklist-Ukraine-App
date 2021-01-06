package com.datapath.checklistukraineapp.exception;

public class ControlEventException extends RuntimeException {

    private final static String MESSAGE_TEMPLATE = "ControlEvent not found. Id %s";

    public ControlEventException(Long id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
