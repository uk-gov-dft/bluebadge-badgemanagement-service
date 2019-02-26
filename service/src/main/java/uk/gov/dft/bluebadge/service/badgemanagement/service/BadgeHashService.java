package uk.gov.dft.bluebadge.service.badgemanagement.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import uk.gov.dft.bluebadge.common.service.exception.InternalServerException;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;

@Slf4j
class BadgeHashService {

  private BadgeHashService() {}

  static byte[] getBadgeEntityHash(BadgeEntity badge) {

    // Do not change hashing without updating existing hash codes in DB.
    return toSha256(
        coalesceValueString(badge.getHolderName())
            + coalesceValueDate(badge.getDob())
            + coalesceValueString(badge.getLocalAuthorityShortCode())
            + coalesceValueString(badge.getNino())
            + coalesceValueString(badge.getContactPostcode())
            + coalesceValueDate(badge.getStartDate())
            + coalesceValueDate(badge.getExpiryDate()));
  }

  static byte[] toSha256(String from) {
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new InternalServerException(e);
    }
    return digest.digest(from.getBytes(StandardCharsets.UTF_8));
  }

  static String coalesceValueString(String value) {
    return null == value ? "X" : value;
  }

  static String coalesceValueDate(LocalDate value) {
    return null == value ? "1970-01-01" : value.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
  }
}
