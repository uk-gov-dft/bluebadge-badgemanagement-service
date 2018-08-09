ALTER TABLE badge DROP COLUMN local_authority_id;
ALTER TABLE badge ADD COLUMN local_authority_short_code VARCHAR(10);
UPDATE badge SET local_authority_short_code = 'ABERD';
ALTER TABLE badge ALTER COLUMN local_authority_short_code SET NOT NULL;

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE badge ADD COLUMN local_authority_id INTEGER;
ALTER TABLE badge DROP COLUMN local_authority_short_code;
