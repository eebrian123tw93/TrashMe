package com.brianlu.trashme.exception;

public class APIException extends RuntimeException {

    private final int code;
    private final String apiErrorMessage;

    public APIException(int code, String message) {
        super(message);
        this.code = code;
        this.apiErrorMessage = message;
    }

    public void printError(){
        System.out.println("Code:" + code + "error message:" + apiErrorMessage);
    }
}
