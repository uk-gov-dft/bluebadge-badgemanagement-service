package uk.gov.dft.bluebadge.service.badgemanagement.service;

import uk.gov.dft.bluebadge.model.badgemanagement.generated.ErrorErrors;

public enum ValidationKeyEnum {
  APP_DATE_IN_PAST(
      "DateInPast.badge.applicationDate",
      "Application date must be in the past.",
      "applicationDate"),
  INVALID_PARTY_CODE("Invalid.badge.partyCode", "Invalid party code.", "partyCode"),
  INVALID_ELIGIBILITY_CODE(
      "Invalid.badge.eligibilityCode", "Invalid eligibility code.", "eligibilityCode"),
  INVALID_DELIVER_TO_CODE(
      "Invalid.badge.deliverToCode", "Invalid deliver to code.", "deliverToCode"),
  INVALID_DELIVER_OPTION_CODE(
      "Invalid.badge.deliverOptionCode", "Invalid delivery option code.", "deliverOptionCode"),
  INVALID_CHANNEL_CODE("Invalid.badge.channelCode", "Invalid channel code.", "appChannelCode"),
  START_DATE_IN_PAST(
      "DateInPast.badge.startDate", "Badge start date cannot be in the past.", "startDate"),
  START_EXPIRY_DATE_RANGE(
      "DateRange.badge.expiryDate",
      "Expiry date must be within 3 years of start date.",
      "expiryDate"),
  INVALID_GENDER_CODE(
      "Invalid.badge.genderCode", "Invalid gender code.", "party.person.genderCode"),
  MISSING_PERSON_OBJECT(
      "NotNull.badge.person", "Person details must be included if party is a person.", "party"),
  MISSING_ORG_OBJECT(
      "NotNull.badge.organisation",
      "Organisation details must be included if party is an organisation.",
      "organisation");

  private final String key;
  private final String defaultMessage;
  private final String field;

  ValidationKeyEnum(String key, String defaultMessage, String field) {

    this.key = key;
    this.defaultMessage = defaultMessage;
    this.field = field;
  }

  public ErrorErrors getErrorInstance() {
    ErrorErrors error = new ErrorErrors();
    error.setField(field);
    error.setMessage(key);
    error.setReason(defaultMessage);
    return error;
  }
}
