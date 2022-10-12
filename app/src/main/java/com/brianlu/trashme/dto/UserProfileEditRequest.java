package com.brianlu.trashme.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileEditRequest {
  private String password;
  private String name;
  private String profilePicUrl;
}
