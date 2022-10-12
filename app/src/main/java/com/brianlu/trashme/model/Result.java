package com.brianlu.trashme.model;

import com.brianlu.trashme.exception.ApiException;

import java.util.Map;

import lombok.Data;

@Data
public class Result {
  private int code;
  private String message;
  private Map<String, Object> payload;

  public void checkAPIResultOk() throws ApiException {
    if (code != 1) {
      throw new ApiException(code, message);
    }
  }

  public void checkPayLoadIsNotNuLL() throws Exception {
    if (payload == null || payload.isEmpty()) {
      throw new Exception("payload is empty");
    }
  }
}
