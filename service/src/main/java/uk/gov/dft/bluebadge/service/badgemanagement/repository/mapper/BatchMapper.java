package uk.gov.dft.bluebadge.service.badgemanagement.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import uk.gov.dft.bluebadge.service.badgemanagement.model.BatchType;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchBadgeLinkEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;

@SuppressWarnings("unused")
@Mapper
public interface BatchMapper {

  BatchEntity createBatch(
      BatchEntity.SourceEnum source, BatchEntity.PurposeEnum purpose, String filename);

  void appendBadgesToBatch(Integer batchId, BatchType batchType);

  int linkBadgeToBatch(BatchBadgeLinkEntity params);

  boolean badgeAlreadyProcessed(String badgeNumber);
}
