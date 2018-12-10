package uk.gov.dft.bluebadge.service.badgemanagement.repository;

import static java.time.temporal.ChronoField.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.AppendBadgesToBatchParams;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.domain.BatchEntity;
import uk.gov.dft.bluebadge.service.badgemanagement.repository.mapper.BatchMapper;

/** Provides CRUD operations on BatchEntity entity. */
@Component
@Slf4j
public class BatchRepository implements BatchMapper {

  class Statements {

    private Statements() {}

    static final String CREATE = "createBatch";
    static final String APPEND_BADGES = "appendBadges";
  }

  private final SqlSession sqlSession;

  public BatchRepository(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  @Override
  public BatchEntity createBatch(String batchType, String source, String purpose) {
    log.debug("Create batch");

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

  @Override
  public void appendBadgesToBatch(Integer batchId, String batchType) {
    AppendBadgesToBatchParams params =
        AppendBadgesToBatchParams.builder().batchId(batchId).batchType(batchType).build();
    sqlSession.insert(Statements.APPEND_BADGES, params);
  }

  private String getFilename(LocalDateTime localDateTime) {
    StringBuilder filename = new StringBuilder().append("BADGEEXTRACT_");
    DateTimeFormatter dateTimeFormatter =
        new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4)
            .appendValue(MONTH_OF_YEAR, 2)
            .appendValue(DAY_OF_MONTH, 2)
            .appendValue(HOUR_OF_DAY, 2)
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendValue(SECOND_OF_MINUTE, 2)
            .toFormatter();
    String localDateTimeString = localDateTime.format(dateTimeFormatter);
    return filename.append(localDateTimeString).toString();
  }
}
