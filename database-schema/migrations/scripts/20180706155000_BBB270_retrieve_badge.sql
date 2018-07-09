
ALTER TABLE badge ADD COLUMN cancel_reason_code VARCHAR(10);
ALTER TABLE badge ADD COLUMN order_date DATE NOT NULL DEFAULT now();

COMMENT ON COLUMN badge.cancel_reason_code IS 'Reason for badge being cancelled.';
COMMENT ON COLUMN badge.order_date IS 'Date badge ordered, same as created.';

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE badge DROP COLUMN cancel_reason_code CASCADE;
ALTER TABLE badge DROP COLUMN order_date CASCADE;