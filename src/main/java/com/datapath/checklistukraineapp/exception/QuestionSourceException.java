package com.datapath.checklistukraineapp.exception;

public class QuestionSourceException extends RuntimeException {

    private final static String MESSAGE_TEMPLATE = "Question Source not found. Id %s";

    public QuestionSourceException(String id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
