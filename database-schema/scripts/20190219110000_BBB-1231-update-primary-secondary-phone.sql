-- // BBB-1231-update-primary-secondary-phone
-- Migration SQL that makes the change goes here.
ALTER TABLE badgemanagement.badge ALTER COLUMN primary_phone_no TYPE VARCHAR(100);
ALTER TABLE badgemanagement.badge ALTER COLUMN secondary_phone_no TYPE VARCHAR(100);

-- //@UNDO
-- SQL to undo the change goes here.

-- Deliberately do not want to truncate columns back down to VARCHAR(20)
