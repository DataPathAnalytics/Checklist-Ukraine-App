package com.datapath.checklistukraineapp.exception;

public class TemplateException extends RuntimeException {

    private final static String MESSAGE_TEMPLATE = "Template not found. Id %s";

    public TemplateException(Long id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
