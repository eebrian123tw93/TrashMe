package com.brianlu.trashme.Exception;

public class APIException extends RuntimeException {

    private int code;
    private String apiErrorMessage;

    public APIException(int code, String message) {
        super(message);
        this.code = code;
        this.apiErrorMessage = message;
    }
}
