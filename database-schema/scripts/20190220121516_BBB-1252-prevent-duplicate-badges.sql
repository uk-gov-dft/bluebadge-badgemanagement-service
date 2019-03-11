-- // BBB-1252-prevent-duplicate-badges
-- Migration SQL that makes the change goes here.
SET search_path = badgemanagement;

ALTER TABLE badgemanagement.badge ADD COLUMN badge_hash BYTEA;

COMMENT ON COLUMN badgemanagement.badge.badge_hash IS
 'Sha-256 hash on holder_name, dob, local_authority_short_code, nino, postcode, start_date, expiry_date. nulls hashed as either `X` or 1970-01-01 based upon type';

CREATE INDEX CONCURRENTLY badge_hash_ix ON badgemanagement.badge(badge_hash, badge_status);
-- //@UNDO
-- SQL to undo the change goes here.
SET search_path = badgemanagement;
DROP INDEX badge_hash_ix;
ALTER TABLE badgemanagement.badge DROP COLUMN badge_hash;

