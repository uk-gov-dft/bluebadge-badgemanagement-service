
-- // BBB-1371-add-not-for-reassessment
-- Migration SQL that makes the change goes here.
ALTER TABLE badgemanagement.badge ADD COLUMN not_for_reassessment BOOLEAN;

-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE badgemanagement.badge DROP COLUMN IF EXISTS not_for_reassessment CASCADE;


