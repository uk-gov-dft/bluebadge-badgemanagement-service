package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;

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
  INVALID_DELIVER_FAST_TO_COUNCIL(
      "Invalid.badge.deliverOptionCode",
      "Only 'standard' delivery option is available when delivering to council.",
      "deliverToCode"),
  INVALID_CHANNEL_CODE("Invalid.badge.channelCode", "Invalid channel code.", "appChannelCode"),
  INVALID_LA_CODE(
      "Invalid.badge.localAuthority", "Invalid local authority code.", "localAuthorityShortCode"),
  START_DATE_IN_PAST(
      "DateInPast.badge.startDate", "PrintBatchBadgeRequest start date cannot be in the past.", "startDate"),
  START_EXPIRY_DATE_RANGE(
      "DateRange.badge.expiryDate",
      "Expiry date must be within 3 years of start date.",
      Constants.EXPIRY_DATE_FIELD),
  INVALID_NUMBER_OF_BADGES_PERSON(
      "Invalid.badge.numberOfBadges", "Number of badges for person should be 1", "numberOfBadges"),
  INVALID_NUMBER_OF_BADGES_ORGANISATION(
      "Invalid.badge.numberOfBadges",
      "Number of badges should be between 1 and 999",
      "numberOfBadges"),
  INVALID_GENDER_CODE(
      "Invalid.badge.genderCode", "Invalid gender code.", "party.person.genderCode"),
  INVALID_CANCEL_CODE(
      "Invalid.badgeCancelRequest.cancelReasonCode", "Invalid cancel code.", "cancelReasonCode"),
  MISSING_PERSON_OBJECT(
      "NotNull.badge.person", "Person details must be included if party is a person.", "party"),
  MISSING_ORG_OBJECT(
      "NotNull.badge.organisation",
      "Organisation details must be included if party is an organisation.",
      "organisation"),
  MISSING_FIND_PARAMS(
      "NotNull.params.badge.find", "To search badges require either name or postcode.", "name"),
  TOO_MANY_FIND_PARAMS(
      "TooMany.params.badge.find",
      "To search badges require either name or postcode, not both.",
      "name"),
  CANCEL_EXPIRY_DATE_IN_PAST(
      "Invalid.badge.cancel.expiryDate",
      "Cannot cancel an expired badge.",
      Constants.EXPIRY_DATE_FIELD),
  CANCEL_STATUS_INVALID(
      "Invalid.badge.cancel.status", "Cannot cancel a badge of this status.", "badgeStatus"),
  CANCEL_FAILED_UNEXPECTED("Unexpected.cancel.fail", "Cancel failed.", "unknown"),
  INVALID_BADGE_NUMBER(
      "Invalid.badgeNumber",
      "Invalid badge number or the body does not match the request.",
      "badgeNumber"),
  REPLACE_INVALID_REASON(
      "Invalid.badge.replace.replaceReason", "Invalid replace reason.", "replaceReason"),
  REPLACE_EXPIRY_DATE_IN_PAST(
      "Invalid.badge.replace.expiryDate",
      "Cannot replace an expired badge.",
      Constants.EXPIRY_DATE_FIELD),
  REPLACE_INVALID_BADGE_STATUS(
      "Invalid.badge.replace.badgeStatus", "Cannot replace a badge of this status.", "badgeStatus");

  private final String key;
  private final String defaultMessage;
  private final String field;

  ValidationKeyEnum(String key, String defaultMessage, String field) {

    this.key = key;
    this.defaultMessage = defaultMessage;
    this.field = field;
  }

  public ErrorErrors getFieldErrorInstance() {
    ErrorErrors error = new ErrorErrors();
    error.setField(field);
    error.setMessage(key);
    error.setReason(defaultMessage);
    return error;
  }

  public Error getSystemErrorInstance() {
    Error error = new Error();
    error.setMessage(key);
    error.setReason(defaultMessage);
    return error;
  }

  private static class Constants {
    public static final String EXPIRY_DATE_FIELD = "expiryDate";
  }
}
