package uk.gov.dft.bluebadge.service.badgemanagement.converter;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts to and from API model and DB Entity Model.
 *
 * @param <E> DB Entity bean
 * @param <M> API Model bean
 */
interface BiConverter<E, M> extends ToModelConverter<E,M>, ToEntityConverter<E, M>{
}
