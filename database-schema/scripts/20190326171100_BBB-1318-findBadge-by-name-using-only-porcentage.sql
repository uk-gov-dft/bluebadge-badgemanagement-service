CREATE INDEX CONCURRENTLY badge_start_date_ix ON badge(start_date);

--//@UNDO
-- SQL to undo the change goes here.
DROP INDEX badge_start_date_ix;
