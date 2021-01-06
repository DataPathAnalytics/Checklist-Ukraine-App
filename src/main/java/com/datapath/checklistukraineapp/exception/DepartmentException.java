package com.datapath.checklistukraineapp.exception;

public class DepartmentException extends RuntimeException {

    private final static String MESSAGE_TEMPLATE = "Department not found. Id %s";

    public DepartmentException(Long id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
