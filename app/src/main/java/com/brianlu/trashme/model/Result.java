package com.brianlu.trashme.model;

import com.brianlu.trashme.exception.APIException;

import lombok.Data;

@Data
public class Result {
    private int code;
    private String message;
    private String payload;

    public void checkAPIResultOk() throws APIException {
        if (code != 1) {
            throw new APIException(code, message);
        }
    }

    public void checkPayLoadIsNotNuLL() throws Exception {
        if (payload == null || payload.isEmpty()) {
            throw new Exception("payload is empty");
        }
    }

}
