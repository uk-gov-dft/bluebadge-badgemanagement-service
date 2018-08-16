
SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

DROP SCHEMA IF EXISTS badgemanagement_unittest CASCADE;
CREATE SCHEMA badgemanagement_unittest;

SET search_path = badgemanagement_unittest;

CREATE TABLE badgemanagement_unittest.badge (
    badge_no character varying(6) NOT NULL,
    badge_status character varying(10),
    party_code character varying(10) NOT NULL,
    local_authority_ref character varying(100),
    app_channel_code character varying(10) NOT NULL,
    start_date date NOT NULL,
    expiry_date date NOT NULL,
    eligibility_code character varying(10),
    image_link character varying(255),
    deliver_to_code character varying(10) NOT NULL,
    deliver_option_code character varying(10) NOT NULL,
    holder_name character varying(100) NOT NULL,
    nino character varying(9),
    dob date,
    gender_code character varying(10),
    contact_name character varying(100),
    contact_building_street character varying(100) NOT NULL,
    contact_line2 character varying(100),
    contact_town_city character varying(100) NOT NULL,
    contact_postcode character varying(8) NOT NULL,
    primary_phone_no character varying(20),
    secondary_phone_no character varying(20),
    contact_email_address character varying(100),
    holder_name_upper character varying(100),
    cancel_reason_code character varying(10),
    order_date date DEFAULT now() NOT NULL,
    app_date date DEFAULT now(),
    local_authority_short_code character varying(10) NOT NULL,
    image_link_original character varying(255)
);

CREATE SEQUENCE badgemanagement_unittest.badge_no
    START WITH 63999999
    INCREMENT BY -1
    NO MINVALUE
    MAXVALUE 63999999
    CACHE 1;

SELECT pg_catalog.setval('badgemanagement_unittest.badge_no', 63999957, true);

ALTER TABLE ONLY badgemanagement_unittest.badge
    ADD CONSTRAINT badge_pkey PRIMARY KEY (badge_no);

CREATE INDEX badge_postcode_ix ON badgemanagement_unittest.badge USING btree (contact_postcode);


-- Test data

insert into badgemanagement_unittest.badge (badge_no, badge_status, party_code,
        local_authority_short_code, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, image_link, image_link_original, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date) values (
        'KKKKKK', 'ISSUED', 'PERSON', 'ABERD', 'to delete', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', 'badge/KKKKKK/thumbnail.jpg', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637FU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01'),
        ('KKKKKD', 'ISSUED', 'PERSON', 'ABERD', 'to retrieve', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637FU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01'),
        ('KKKKKA', 'ISSUED', 'PERSON', 'ABERD', 'to update', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637FU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01');
