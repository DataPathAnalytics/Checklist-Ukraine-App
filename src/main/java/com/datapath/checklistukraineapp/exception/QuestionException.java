package com.datapath.checklistukraineapp.exception;

public class QuestionException extends RuntimeException {

    private final static String MESSAGE_TEMPLATE = "Question not found. Id %s";

    public QuestionException(Long id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
