-- TODO remove next deletes once they have had time to percolate through environments
DELETE FROM users WHERE name LIKE '%Sampath%';
DELETE FROM users WHERE name = 'nobody';
delete from badge where badge_no = 'KKKKKE';
-- TODO end.

DELETE FROM users WHERE email_address = 'createuservalid@dft.gov.uk';
DELETE FROM users WHERE id < 0;
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-1, 'Sampath', 'abc@dft.gov.uk', 2, 2);
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-2, 'Sampath', 'def@dft.gov.uk', 2, 2);
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-3, 'nobody', 'abcnobody@dft.gov.uk', 2, 2);
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-4, 'update test', 'updateme@dft.gov.uk', 2, 2);
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-5, 'delete test', 'deleteme@dft.gov.uk', 2, 2);

insert into badge
(badge_no, badge_status, party_code,
        local_authority_id, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, image_link, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date)
values
('KKKKKE', 'ISSUED', 'PERSON', 3, 'to cancel', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', 'HOME', 'STAND', 'Reginald Pai', '', '1953-09-12', 'MALE',
'contact name', 'building and street', '', 'Town or city', 'S637FU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01');
