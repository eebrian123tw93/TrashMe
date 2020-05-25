package com.brianlu.trashme.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

  private int code;
  private String apiErrorMessage;

  public ApiException(int code, String message) {
    super(message);
    this.code = code;
    this.apiErrorMessage = message;
  }
}
