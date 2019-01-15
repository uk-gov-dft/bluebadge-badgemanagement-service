
-- // BBB-1003-process-batches
-- Migration SQL that makes the change goes here.
ALTER TABLE badgemanagement.batch_badge DROP CONSTRAINT batch_badge_badge_no_fk;
ALTER TABLE badgemanagement.batch_badge
  DROP CONSTRAINT batch_badge_batch_id_fk;
ALTER TABLE badgemanagement.batch_badge
  ADD CONSTRAINT batch_badge_batch_id_fk
    FOREIGN KEY (batch_id)
      REFERENCES badgemanagement.batch(batch_id)
      ON DELETE CASCADE;
ALTER TABLE badgemanagement.batch_badge DROP CONSTRAINT batch_badge_pkey;
CREATE INDEX ix_badge_batch_badge_no ON badgemanagement.batch_badge(badge_no);


-- //@UNDO
DELETE FROM badgemanagement.batch_badge
  WHERE badge_no NOT IN (SELECT badge_no FROM badgemanagement.badge);
ALTER TABLE badgemanagement.batch_badge
  ADD CONSTRAINT batch_badge_badge_no_fk
    FOREIGN KEY (badge_no)
      REFERENCES badgemanagement.badge(badge_no);

ALTER TABLE badgemanagement.batch_badge
  DROP CONSTRAINT batch_badge_batch_id_fk;
ALTER TABLE badgemanagement.batch_badge
  ADD CONSTRAINT batch_badge_batch_id_fk
    FOREIGN KEY (batch_id)
      REFERENCES badgemanagement.batch(batch_id);
DROP INDEX IF EXISTS ix_badge_batch_badge_no;

