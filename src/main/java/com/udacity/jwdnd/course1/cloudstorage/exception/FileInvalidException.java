package com.udacity.jwdnd.course1.cloudstorage.exception;

public class FileInvalidException extends RuntimeException {

    public FileInvalidException(String message) {
        super(message);
    }

    public FileInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}