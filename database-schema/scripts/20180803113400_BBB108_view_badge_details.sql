ALTER TABLE badge ALTER COLUMN local_authority_id DROP NOT NULL;
ALTER TABLE badge ADD COLUMN local_authority_short_code VARCHAR(10);

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE badge ALTER COLUMN local_authority_id SET NOT NULL;
ALTER TABLE badge DROP COLUMN local_authority_short_code;
