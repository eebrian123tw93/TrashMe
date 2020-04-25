package com.brianlu.trashme.Model;

import com.brianlu.trashme.Exception.APIException;

import lombok.Data;

@Data
public class Result<T> {
    int code;
    String message;
    T playload;

    public void checkAPIResultOk() throws APIException {
        if (code != 1) {
            throw new APIException(code, message);
        }
    }
}
