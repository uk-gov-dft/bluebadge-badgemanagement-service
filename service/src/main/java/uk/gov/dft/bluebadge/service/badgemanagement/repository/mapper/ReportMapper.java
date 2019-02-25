package uk.gov.dft.bluebadge.service.badgemanagement.repository.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import uk.gov.dft.bluebadge.service.badgemanagement.model.IssuedBadge;
import uk.gov.dft.bluebadge.service.badgemanagement.model.ReportSearch;

@Mapper
public interface ReportMapper {
  List<IssuedBadge> findIssuedBadges(ReportSearch reportSearch);
}
