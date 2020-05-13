package com.brianlu.trashme.model;

import lombok.Data;

@Data
public class MainPageModel {
  double recycleTrashPrice;
  double normalTrashPrice;
  double mixedTrashPrice;
  UserInfoExtended userInfoExtended;

  @Data
  static public class UserInfoExtended {
    String locationName;
    Double latitude;
    Double longitude;
    int pickupOrderTimes;
    String name;
    String profilePicUrl;
  }

}

