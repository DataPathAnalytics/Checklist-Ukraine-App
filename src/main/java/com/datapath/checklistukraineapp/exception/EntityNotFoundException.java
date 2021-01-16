package com.datapath.checklistukraineapp.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "%s not found, id %s";

    public EntityNotFoundException(String entityName, Object id) {
        super(String.format(MESSAGE_TEMPLATE, entityName, id));
    }
}
