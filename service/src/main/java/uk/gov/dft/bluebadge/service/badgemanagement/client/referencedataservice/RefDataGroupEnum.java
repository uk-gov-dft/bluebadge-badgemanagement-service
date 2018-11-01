package uk.gov.dft.bluebadge.service.badgemanagement.client.referencedataservice;

@SuppressWarnings("SpellCheckingInspection")
public enum RefDataGroupEnum {
  COUNCIL("LC"),
  NATION("NATION");

  public String getGroupKey() {
    return groupKey;
  }

  private final String groupKey;

  RefDataGroupEnum(String groupKey) {

    this.groupKey = groupKey;
  }
}
