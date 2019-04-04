-- // BBB-1117-batch-additional-fields
-- Migration SQL that makes the change goes here.
ALTER TABLE badgemanagement.batch_badge
   ADD COLUMN local_authority_short_code VARCHAR(10)
  ,ADD COLUMN issued_date_time TIMESTAMP WITHOUT TIME ZONE
  ,ADD COLUMN rejected_reason TEXT
  ,ADD COLUMN cancellation VARCHAR(10)
;

CREATE INDEX badge_print_ix ON badgemanagement.badge(badge_status, deliver_option_code, deliver_to_code);


-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE badgemanagement.batch_badge
   DROP COLUMN local_authority_short_code
  ,DROP COLUMN issued_date_time
  ,DROP COLUMN rejected_reason
  ,DROP COLUMN cancellation
;

DROP INDEX badgemanagement.badge_print_ix;

