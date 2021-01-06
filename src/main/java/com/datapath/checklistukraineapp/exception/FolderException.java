package com.datapath.checklistukraineapp.exception;

public class FolderException extends RuntimeException {

    private final static String MESSAGE_TEMPLATE = "Folder not found. Id %s";

    public FolderException(Long id) {
        super(String.format(MESSAGE_TEMPLATE, id));
    }
}
