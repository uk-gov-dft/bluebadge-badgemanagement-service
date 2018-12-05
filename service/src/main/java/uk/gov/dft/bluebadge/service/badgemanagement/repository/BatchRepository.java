package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static java.time.temporal.ChronoField.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.mapper.BatchMapper;

/** Provides CRUD operations on BatchEntity entity. */
@Component
@Slf4j
public class BatchRepository implements BatchMapper {

  class Statements {

    private Statements() {}

    static final String CREATE = "createBatch";
    /*  static final String FIND = "findBadges";
      static final String RETRIEVE = "retrieveBadge";
      static final String CANCEL = "cancelBadge";
      static final String DELETE = "deleteBadge";
      static final String REPLACE = "replaceBadge";
      static final String FIND_BADGES_FOR_PRINT_BATCH = "findBadgesForPrintBatch";
    */
  }

  private final SqlSession sqlSession;

  public BatchRepository(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  @Override
  public BatchEntity createBatch(String batchType, String source, String purpose) {
    log.debug("Create batch");

    // purpose is ISSUED, REJECTED, PRINT
    // source DFT, THIRDPARTY
    // BADGEEXTRACT180901191635.xml

    LocalDateTime localDateTime = LocalDateTime.now();

    BatchEntity batch =
        BatchEntity.builder()
            .created(localDateTime)
            .filename(getFilename(localDateTime))
            .purpose(purpose)
            .source(source)
            .build();
    sqlSession.insert(Statements.CREATE, batch);
    return batch;
  }

  private String getFilename(LocalDateTime localDateTime) {
    StringBuilder filename = new StringBuilder().append("BADGEEXTRACT");
    DateTimeFormatter dateTimeFormatter =
        new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4)
            .appendValue(MONTH_OF_YEAR, 2)
            .appendValue(DAY_OF_MONTH, 2)
            .appendValue(HOUR_OF_DAY, 2)
            .appendValue(MINUTE_OF_HOUR, 2)
            .toFormatter();
    String localDateTimeString = localDateTime.format(dateTimeFormatter);
    return filename.append(localDateTimeString).toString();
  }
}
