package com.brianlu.trashme.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomResponse<T> {
  private int code;
  private String message;
  private T payload;
}
