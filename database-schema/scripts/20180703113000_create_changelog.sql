CREATE TABLE changelog (
  ID NUMERIC(20,0) NOT NULL,
  APPLIED_AT VARCHAR(25) NOT NULL,
  DESCRIPTION VARCHAR(255) NOT NULL);


ALTER TABLE changelog
ADD CONSTRAINT PK_changelog
PRIMARY KEY (ID);

--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE IF EXISTS changelog;

