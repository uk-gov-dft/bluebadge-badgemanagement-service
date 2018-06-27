package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.dft.bluebadge.service.badgemanagement.BadgeManagementServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BadgeManagementServiceApplication.class)
public class BadgeManagementRepositoryTest {

  @Autowired private BadgeManagementRepository badgeManagementRepository;

  @Test
  public void aTest() {
    /*
    // Given the authority has no users
    int authorityId = -1;
    UserEntity queryParams = new UserEntity();
    queryParams.setLocalAuthorityId(authorityId);

    // When users are retrieved for the authority
    List<UserEntity> userEntityList =
        badgeManagementRepository.retrieveUsersByAuthorityId(queryParams);

    // Then an empty list is returned
    Assert.assertEquals(0, userEntityList.size());
    */
  }
}
