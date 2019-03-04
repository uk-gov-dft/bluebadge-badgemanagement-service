-- // BBB-1252-prevent-duplicate-badges
-- Migration SQL that makes the change goes here.
SET search_path = badgemanagement;

ALTER TABLE badgemanagement.badge ADD COLUMN badge_hash BYTEA;
CREATE INDEX badge_hash_ix ON badgemanagement.badge(badge_hash, badge_status);
COMMENT ON COLUMN badgemanagement.badge.badge_hash IS
 'Sha-256 hash on holder_name, dob, local_authority_short_code, nino, postcode, start_date, expiry_date. nulls hashed as either `X` or 1970-01-01 based upon type';

UPDATE badgemanagement.badge SET badge_hash =
 public.digest(concat(holder_name, to_char(COALESCE(dob, '1970-01-01'::DATE), 'YYYY-MM-DD'),
               COALESCE(local_authority_short_code, 'X'), COALESCE(nino, 'X'), COALESCE(contact_postcode, 'X'),
               to_char(start_date, 'YYYY-MM-DD'), to_char(expiry_date, 'YYYY-MM-DD')), 'sha256') WHERE badge_status NOT IN ('DELETED');

-- //@UNDO
-- SQL to undo the change goes here.
SET search_path = badgemanagement;
DROP INDEX badge_hash_ix;
ALTER TABLE badgemanagement.badge DROP COLUMN badge_hash;

