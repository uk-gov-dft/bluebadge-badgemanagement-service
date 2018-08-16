ALTER TABLE badgemanagement.badge ADD COLUMN image_link_original VARCHAR(255);
ALTER TABLE badgemanagement.badge ALTER COLUMN badge_status SET default null;
COMMENT ON COLUMN badgemanagement.badge.image_link_original IS 'URL to the origionally uploaded photo.';

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE badgemanagement.badge DROP COLUMN image_link_original;
