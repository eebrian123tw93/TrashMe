package com.brianlu.trashme.model;

import lombok.Data;

@Data
public class LocationModel {
  Double latitude;
  Double longitude;
  String locationName;
  String userType;
}
