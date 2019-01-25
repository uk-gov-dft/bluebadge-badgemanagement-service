SET search_path = badgemanagement;

DELETE FROM badge WHERE holder_name LIKE ('BatchTestData%');
DELETE FROM batch WHERE batch_filename
  IN ('ValidConfirmation2Badges.xml','ValidRejection2Badges.xml','ConfirmationBadgeNotExist (2).xml');
insert into badge
(badge_no, badge_status, party_code,
        local_authority_short_code, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, image_link, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date)
values
('CONF01', 'PROCESSED', 'PERSON',
 'ANGL', 'Print will be confirmed.',
 'ONLINE','2025-05-01', '2028-05-01',
 'PIP', '', 'HOME',
 'STAND', 'BatchTestData ToBe Confirmed', '',
 '1953-09-12', 'MALE', 'contact name',
 'building and street', '', 'Town or city',
 'S637FU', '020 7014 0800', null,
 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01');

insert into badge
(badge_no, badge_status, party_code,
        local_authority_short_code, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, image_link, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date)
values
('CONF02', 'PROCESSED', 'PERSON',
 'ANGL', 'Print will be confirmed.',
 'ONLINE','2025-05-01', '2028-05-01',
 'PIP', '', 'HOME',
 'STAND', 'BatchTestData ToBe Confirmed2', '',
 '1953-09-12', 'MALE', 'contact name',
 'building and street', '', 'Town or city',
 'S637FU', '020 7014 0800', null,
 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01');

insert into badge
(badge_no, badge_status, party_code,
        local_authority_short_code, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, image_link, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date)
values
('REJ001', 'PROCESSED', 'PERSON',
 'ABERD', 'to be rejected',
 'ONLINE','2025-05-01', '2028-05-01',
 'PIP', '', 'HOME',
 'STAND', 'BatchTestData RejectMe', '',
 '1953-09-12', 'MALE', 'contact name',
 'building and street', '', 'Town or city',
 'S637FU', '020 7014 0800', null,
 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01');

insert into badge
(badge_no, badge_status, party_code,
        local_authority_short_code, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, image_link, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date)
values
('REJ002', 'PROCESSED', 'PERSON',
 'ABERD', 'to be rejected',
 'ONLINE','2025-05-01', '2028-05-01',
 'PIP', '', 'HOME',
 'STAND', 'BatchTestData RejectMe2', '',
 '1953-09-12', 'MALE', 'contact name',
 'building and street', '', 'Town or city',
 'S637FU', '020 7014 0800', null,
 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01');

insert into badge
(badge_no, badge_status, party_code,
        local_authority_short_code, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, image_link, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date)
values
('UNC001', 'PROCESSED', 'PERSON',
 'ABERD', 'no response from print',
 'ONLINE','2025-05-01', '2028-05-01',
 'PIP', '', 'HOME',
 'STAND', 'BatchTestData Unchanged', '',
 '1953-09-12', 'MALE', 'contact name',
 'building and street', '', 'Town or city',
 'S637FU', '020 7014 0800', null,
 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01');