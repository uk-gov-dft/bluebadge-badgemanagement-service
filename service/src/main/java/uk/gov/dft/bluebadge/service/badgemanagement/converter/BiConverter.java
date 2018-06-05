package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import uk.gov.dft.bluebadge.model.badgemanagement.Data;

/**
 * Converts to and from API model and DB Entity Model.
 *
 * @param <ENTITYT> DB Entity bean
 * @param <MODELT> API Model bean
 * @param <DATAT> API Model bean extending Data
 */
interface BiConverter<ENTITYT, MODELT, DATAT extends Data> {
  ENTITYT convertToEntity(MODELT model);

  MODELT convertToModel(ENTITYT entity);

  DATAT convertToData(ENTITYT entity, int totalItems, int updates, int deletes);
}
