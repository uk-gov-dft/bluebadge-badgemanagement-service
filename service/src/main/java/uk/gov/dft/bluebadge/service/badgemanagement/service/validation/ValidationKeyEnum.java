package uk.gov.dft.bluebadge.service.badgemanagement.service.validation;

import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;

public enum ValidationKeyEnum {
  INVALID_NOT_FOR_REASSESSMENT_FOR_ORG(
      "Organisation.invalid.badge.notForReassessment",
      "Invalid value not for reassessment.",
      "notForReassessment"),
  INVALID_NOT_FOR_REASSESSMENT_FOR_AUTOMATIC_ELIGIBILITY(
      "Person.invalid.badge.notForReassessment",
      "Invalid value not for reassessment.",
      "notForReassessment"),
  APP_DATE_IN_PAST(
      "DateInPast.badge.applicationDate",
      "Application date must be in the past.",
      "applicationDate"),
  DOB_IN_PAST("DateInPast.badge.dob", "DOB must be in the past.", "dob"),
  INVALID_PARTY_CODE("Invalid.badge.partyCode", "Invalid party code.", "partyCode"),
  INVALID_ELIGIBILITY_CODE(
      "Invalid.badge.eligibilityCode", "Invalid eligibility code.", Constants.ELIGIBILITY_CODE),
  NULL_ELIGIBILITY_CODE_PERSON(
      "NotNull.badge.eligibilityCode",
      "Eligibility code is mandatory for Person badges.",
      Constants.ELIGIBILITY_CODE),
  NULL_CONTACT_NAME_ORG(
      "NotNull.badge.party.contact.fullName",
      "Contact name is mandatory for organisation badges.",
      "fullName"),
  NULL_DOB_PERSON("NotNull.badge.party.person.dob", "DOB must not be null", "dob"),
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
      "DateInPast.badge.startDate", "Badge start date cannot be in the past.", "startDate"),
  START_EXPIRY_DATE_RANGE(
      "DateRange.badge.expiryDate",
      "Expiry date must be within 3 years of start date.",
      Constants.EXPIRY_DATE),
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
      "Invalid.badge.cancel.expiryDate", "Cannot cancel an expired badge.", Constants.EXPIRY_DATE),
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
      Constants.EXPIRY_DATE),
  REPLACE_INVALID_BADGE_STATUS(
      "Invalid.badge.replace.badgeStatus", "Cannot replace a badge of this status.", "badgeStatus"),
  INVALID_ELIGIBILITY_FOR_NATION(
      "InvalidNation.badge.eligibilityCode",
      "Eligibility type not valid for nation.",
      Constants.ELIGIBILITY_CODE),
  EXPIRY_DATE_IN_PAST(
      "Invalid.badge.expiryDate", "Expiry date cannot be in the past.", Constants.EXPIRY_DATE);

  private final String key;
  private final String defaultMessage;
  private final String field;

  ValidationKeyEnum(String key, String defaultMessage, String field) {

    this.key = key;
    this.defaultMessage = defaultMessage;
    this.field = field;
  }

  public ErrorErrors getFieldErrorInstance() {
    return getFieldErrorInstance(defaultMessage);
  }

  public ErrorErrors getFieldErrorInstance(String reason) {
    ErrorErrors error = new ErrorErrors();
    error.setField(field);
    error.setMessage(key);
    error.setReason(reason);
    return error;
  }

  public Error getSystemErrorInstance() {
    Error error = new Error();
    error.setMessage(key);
    error.setReason(defaultMessage);
    return error;
  }

  private static class Constants {
    static final String EXPIRY_DATE = "expiryDate";
    static final String ELIGIBILITY_CODE = "eligibilityCode";
  }

  public String getKey() {
    return key;
  }
}
