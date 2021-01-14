package com.datapath.checklistukraineapp.exception;

public class ChecklistException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Checklist with id %s not found";

    public ChecklistException(Long id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
