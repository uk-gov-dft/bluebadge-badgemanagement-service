SET search_path = badgemanagement;

DELETE FROM badge WHERE holder_name LIKE ('TestData%');

insert into badge
(badge_no, badge_status, party_code,
        local_authority_short_code, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, not_for_reassessment, image_link, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date)
values
('BBBBBB', 'ISSUED', 'PERSON',
 'ANGL', 'to cancel Diff authority',
 'ONLINE','2025-05-01', '2028-05-01',
 'PIP', null, '', 'HOME',
 'STAND', 'TestData Reginald', '',
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
('BBBBBC', 'ISSUED', 'PERSON',
 'ABERD', 'to cancel ISSUED',
 'ONLINE','2025-05-01', '2028-05-01',
 'PIP', '', 'HOME',
 'STAND', 'TestData Reginald', '',
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
('BBBBBD', 'CANCELLED', 'PERSON',
 'ABERD', 'cant cancel CANCELLED',
 'ONLINE','2025-05-01', '2028-05-01',
 'PIP', '', 'HOME',
 'STAND', 'TestData Reginald', '',
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
('CCCCCC', 'CANCELLED', 'PERSON',
 'ABERD', 'to cancel',
 'ONLINE','2025-05-01', '2028-05-01',
 'PIP', '', 'HOME',
 'STAND', 'TestData Reginald', '',
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
('DDDDDD', 'ISSUED', 'PERSON',
 'ABERD', 'to replace expired',
 'ONLINE','2010-05-01', '2011-05-01',
 'PIP', '', 'HOME',
 'STAND', 'TestData Reginald', '',
 '1953-09-12', 'MALE', 'contact name',
 'building and street', '', 'Town or city',
 'S637FU', '020 7014 0800', null,
 'test101@mailinator.com', 'REGINALD', '2009-07-01', ' 2009-06-01');

insert into badge
(badge_no, badge_status, party_code,
        local_authority_short_code, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, image_link, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date, replace_reason_code)
values
('EEEEEE', 'REPLACED', 'PERSON',
 'ABERD', 'replaced',
 'ONLINE','2010-05-01', '2011-05-01',
 'PIP', '', 'HOME',
 'STAND', 'TestData Reginald', '',
 '1953-09-12', 'MALE', 'contact name',
 'building and street', '', 'Town or city',
 'S637FU', '020 7014 0800', null,
 'test101@mailinator.com', 'REGINALD', '2009-07-01', ' 2009-06-01', 'DAMAGED');

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
('EEEEEF', 'ISSUED', 'PERSON',
 'ABERD', 'to replace success',
 'ONLINE','2010-05-01', '2040-05-01',
 'PIP', '', 'HOME',
 'STAND', 'TestData Reginald', '',
 '1953-09-12', 'MALE', 'contact name',
 'building and street', '', 'Town or city',
 'S637FU', '020 7014 0800', null,
 'test101@mailinator.com', 'REGINALD', '2009-07-01', ' 2009-06-01');

 insert into badge
(badge_no, badge_status, party_code,
        local_authority_short_code, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, not_for_reassessment, image_link, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date)
values
('BBBBBE', 'ISSUED', 'PERSON',
 'ABERD', 'to cancel ISSUED with lower case',
 'ONLINE','2025-05-01', '2028-05-01',
 'CHILDBULK', false, '', 'HOME',
 'STAND', 'TestData Reginald', '',
 '1953-09-12', 'MALE', 'contact name',
 'building and street', '', 'Town or city',
 'S637FU', '020 7014 0800', null,
 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01');

insert into badge
    (badge_no, badge_status, party_code,
     local_authority_short_code, local_authority_ref,
     app_channel_code, start_date, expiry_date,
     eligibility_code, not_for_reassessment, image_link, deliver_to_code,
     deliver_option_code, holder_name, nino,
     dob, gender_code, contact_name,
     contact_building_street, contact_line2, contact_town_city,
     contact_postcode, primary_phone_no, secondary_phone_no,
     contact_email_address, holder_name_upper, order_date, app_date)
values
       ('EEEEEA', 'ISSUED', 'PERSON',
        'ABERD', 'to replace success with lower case',
        'ONLINE','2010-05-01', '2040-05-01',
        'CHILDBULK', true, '', 'HOME',
        'STAND', 'TestData Reginald', '',
        '1953-09-12', 'MALE', 'contact name',
        'building and street', '', 'Town or city',
        'S637FU', '020 7014 0800', null,
        'test101@mailinator.com', 'REGINALD', '2009-07-01', ' 2009-06-01');

