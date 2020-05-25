package com.brianlu.trashme.model;

import lombok.Data;

@Data
public class LocationModel {
  Double latitude;
  Double longitude;
  String locationName;
  String userType;

  public double getDistanceOfMeter(LocationModel model) {

    if (latitude == null || longitude == null || model.latitude == null || model.longitude == null)
      return 0;

    final int R = 6371; // Radius of the earth
    double latDistance = Math.toRadians(model.latitude - latitude);
    double lonDistance = Math.toRadians(model.longitude - longitude);
    double a =
        Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(latitude))
                * Math.cos(model.latitude)
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c * 1000; // distance
  }
}
