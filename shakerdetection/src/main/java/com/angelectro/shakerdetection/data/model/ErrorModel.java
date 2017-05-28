package com.angelectro.shakerdetection.data.model;


public class ErrorModel {
    private final String message;
    private final Throwable throwable;

    public ErrorModel(String message, Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
