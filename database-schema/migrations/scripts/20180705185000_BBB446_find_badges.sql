
ALTER TABLE badge ADD holder_name_upper VARCHAR(100);
COMMENT ON COLUMN badge.holder_name_upper IS 'For searching';

UPDATE badge SET holder_name_upper = UPPER(holder_name);

CREATE INDEX badge_postcode_ix ON badge(contact_postcode);
CREATE INDEX badge_la_id_ix ON badge(local_authority_id);
--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE badge DROP COLUMN holder_name_upper CASCADE;
DROP INDEX badge_postcode_ix;
DROP INDEX badge_la_id_ix;