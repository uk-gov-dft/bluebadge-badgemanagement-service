package uk.gov.dft.bluebadge.service.badgemanagement.converter;

public interface ToEntityConverter<E, M> {
  E convertToEntity(M model);
}
