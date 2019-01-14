
-- // BBB-1003-process-batches
-- Migration SQL that makes the change goes here.
ALTER TABLE badgemanagement.batch_badge DROP CONSTRAINT batch_badge_badge_no_fk;


-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE badgemanagement.batch_badge
  ADD CONSTRAINT batch_badge_badge_no_fk
    FOREIGN KEY (badge_no)
      REFERENCES badgemanagement.badge(badge_no);

