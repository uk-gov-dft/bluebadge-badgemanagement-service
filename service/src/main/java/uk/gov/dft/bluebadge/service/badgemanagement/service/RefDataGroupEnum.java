package uk.gov.dft.bluebadge.service.badgemanagement.service;

public enum RefDataGroupEnum {
  ELIGIBILITY("ELIGIBILIT"),
  APP_SOURCE("APPSOURCE"),
  PARTY("PARTY"),
  STATUS("STATUS"),
  DELIVER_TO("DELIVER"),
  DELIVERY_OPTIONS("DELOP"),
  GENDER("GENDER");

  public String getGroupKey() {
    return groupKey;
  }

  private String groupKey;

  RefDataGroupEnum(String groupKey) {

    this.groupKey = groupKey;
  }
}
