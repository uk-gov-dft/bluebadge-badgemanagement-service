package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import java.util.ArrayList;
import java.util.List;

public interface ToEntityConverter<E, M> {
  E convertToEntity(M model);
}
