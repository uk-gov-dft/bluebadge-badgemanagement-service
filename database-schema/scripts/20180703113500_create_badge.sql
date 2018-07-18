
CREATE TABLE badge (
  badge_no              VARCHAR(6)   NOT NULL,
  badge_status          VARCHAR(10)  DEFAULT 'NEW',
  party_code            VARCHAR(10)  NOT NULL,
  local_authority_id    INTEGER      NOT NULL,
  local_authority_ref   VARCHAR(100),
  app_datetime          TIMESTAMP    NOT NULL,
  app_channel_code      VARCHAR(10)  NOT NULL,
  start_date            DATE         NOT NULL,
  expiry_date           DATE         NOT NULL,
  eligibility_code      VARCHAR(10),
  image_link            VARCHAR(255),
  deliver_to_code       VARCHAR(10)  NOT NULL,
  deliver_option_code   VARCHAR(10)  NOT NULL,
  holder_name           VARCHAR(100) NOT NULL,
  nino                  VARCHAR(9),
  dob                   DATE,
  gender_code           VARCHAR(10),
  contact_name          VARCHAR(100),
  contact_building_street VARCHAR(100) NOT NULL,
  contact_line2         VARCHAR(100),
  contact_town_city     VARCHAR(100) NOT NULL,
  contact_postcode      VARCHAR(8)   NOT NULL,
  primary_phone_no      VARCHAR(20),
  secondary_phone_no    VARCHAR(20),
  contact_email_address VARCHAR(100),

  PRIMARY KEY(badge_no)
);

COMMENT ON TABLE badge IS 'Holds blue badge record.';
COMMENT ON COLUMN badge.badge_no IS 'Unique badge number assigned on create/order.';
COMMENT ON COLUMN badge.party_code IS 'Person or organisation ref code.';
COMMENT ON COLUMN badge.local_authority_id IS 'Local authority administering badge.';
COMMENT ON COLUMN badge.local_authority_ref IS 'Reference entered by LA.';
COMMENT ON COLUMN badge.app_datetime IS 'Application received date and time.';
COMMENT ON COLUMN badge.app_channel_code IS 'Application channel, e.g. online, phone...';
COMMENT ON COLUMN badge.start_date IS 'Valid from date.';
COMMENT ON COLUMN badge.expiry_date IS 'Expires on date.';
COMMENT ON COLUMN badge.eligibility_code IS 'Ref data code of eligibility reason.';
COMMENT ON COLUMN badge.image_link IS 'Link to badge holder photo.';
COMMENT ON COLUMN badge.deliver_to_code IS 'Ref data, holder or LA.';
COMMENT ON COLUMN badge.deliver_option_code IS 'Ref data. Normal or fast.';
COMMENT ON COLUMN badge.holder_name IS 'Badge holder name, for person or organisation.';
COMMENT ON COLUMN badge.nino IS 'If person, national insurance number.';
COMMENT ON COLUMN badge.dob IS 'If person, date of birth.';
COMMENT ON COLUMN badge.gender_code IS 'MALE, FEMALE.';
COMMENT ON COLUMN badge.contact_name IS 'Application contact name for organisation.';
COMMENT ON COLUMN badge.contact_building_street IS 'Applicant address line 1.';
COMMENT ON COLUMN badge.contact_line2 IS 'Applicant address line 2.';
COMMENT ON COLUMN badge.contact_town_city IS 'Applicant town or city (or hamlet etc.).';
COMMENT ON COLUMN badge.contact_postcode IS 'Applicant postcode.';
COMMENT ON COLUMN badge.primary_phone_no IS 'Applicant main contact number.';
COMMENT ON COLUMN badge.secondary_phone_no IS 'Applicant secondary contact number.';
COMMENT ON COLUMN badge.contact_email_address IS 'Applicant email address.';

CREATE SEQUENCE badge_no INCREMENT -1 START WITH 63999999 MAXVALUE 63999999;

--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE IF EXISTS badge;
DROP SEQUENCE IF EXISTS badge_no;

