ALTER TABLE badge DROP COLUMN local_authority_id;
ALTER TABLE badge ADD COLUMN local_authority_short_code VARCHAR(10) NOT NULL;

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE badge ADD COLUMN local_authority_id INTEGER NOT NULL;
ALTER TABLE badge DROP COLUMN local_authority_short_code;
