package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import java.util.ArrayList;
import java.util.List;
import uk.gov.dft.bluebadge.service.badgemanagement.service.exception.BadRequestException;

/**
 * Converts to and from API model and DB Entity Model.
 *
 * @param <ENTITYT> DB Entity bean
 * @param <MODELT> API Model bean
 */
interface BiConverter<ENTITYT, MODELT> {
  ENTITYT convertToEntity(MODELT model) throws BadRequestException;

  MODELT convertToModel(ENTITYT entity);

  default List<MODELT> convertToModelList(List<ENTITYT> entityList) {
    List<MODELT> modelList = new ArrayList<>();
    for (ENTITYT entityItem : entityList) {
      modelList.add(convertToModel(entityItem));
    }
    return modelList;
  }
}
