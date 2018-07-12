package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.CREATE;
import static uk.gov.dft.bluebadge.service.badgemanagement.repository.BadgeManagementRepository.Statements.FIND;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BadgeEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.FindBadgeParams;

public class BadgeManagementRepositoryTest {

  @Mock SqlSession sqlSession;

  BadgeManagementRepository repository;

  public BadgeManagementRepositoryTest() {
    MockitoAnnotations.initMocks(this);
    repository = new BadgeManagementRepository(sqlSession);
  }

  @Test
  public void createBadge() {
    repository.createBadge(BadgeEntity.builder().build());
    verify(sqlSession).insert(eq(CREATE), any());
  }

  @Test
  public void findBadges() {
    FindBadgeParams params = FindBadgeParams.builder().name("%Bob%").build();
    repository.findBadges(params);
    verify(sqlSession).selectList(eq(FIND), eq(params));
  }
}
