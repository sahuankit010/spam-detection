package com.spamdetector.exception;

public class DuplicateSpamReportException extends RuntimeException {
    public DuplicateSpamReportException(String message) {
        super(message);
    }
}
