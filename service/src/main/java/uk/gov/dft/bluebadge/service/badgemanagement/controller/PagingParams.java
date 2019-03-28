package uk.gov.dft.bluebadge.service.badgemanagement.controller;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Data;

@Data
public class PagingParams {

  public static final Integer DEFAULT_PAGE_NUM = 1;
  public static final Integer DEFAULT_PAGE_SIZE = 50;

  @Min(1)
  private Integer pageNum = DEFAULT_PAGE_NUM;

  @Min(1)
  @Max(200)
  private Integer pageSize = DEFAULT_PAGE_SIZE;
}
