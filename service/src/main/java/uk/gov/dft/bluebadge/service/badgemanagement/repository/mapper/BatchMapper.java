package uk.gov.dft.bluebadge.service.badgemanagement.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;

@SuppressWarnings("unused")
@Mapper
public interface BatchMapper {

  BatchEntity createBatch(String batchType, String source, String purpose);
}
