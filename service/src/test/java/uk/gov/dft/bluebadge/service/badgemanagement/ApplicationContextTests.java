package uk.gov.dft.bluebadge.service.badgemanagement;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
  classes = BadgeManagementServiceApplication.class,
  properties = {"management.server.port=19991"}
)
@ActiveProfiles({"test", "dev"})
public abstract class ApplicationContextTests {}
