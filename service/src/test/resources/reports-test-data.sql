delete from badgemanagement.batch where batch_id in (-10, -11, -12, -13, -14);

delete from badgemanagement.badge where badge_no in ('KAKKKK', 'KAKKKD', 'KAKKKA', 'KAKKKC', 'KAKKKE', 'KAKKKB');

insert into badgemanagement.badge (badge_no, badge_status, party_code,
        local_authority_short_code, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, image_link, image_link_original, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date) values (
        'KAKKKK', 'ISSUED', 'PERSON', 'TEST_1', 'to delete', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', 'badge/KKKKKK/thumbnail.jpg', 'badge/KKKKKK/original.jpg', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637FU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KAKKKD', 'REJECT', 'PERSON', 'TEST_2', 'to retrieve', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637DU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KAKKKA', 'ISSUED', 'PERSON', 'TEST_1', 'to update', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KAKKKC', 'DELETED', 'PERSON', 'TEST_1', 'deleted', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637FU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KAKKKE', 'CANCELLED', 'PERSON', 'TEST_4', 'to find badge for print batch', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'FAST', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test106@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KAKKKB', 'REJECT', 'PERSON', 'TEST_2', 'to find badge for print batch', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
;

insert into badgemanagement.batch (batch_id, batch_filename, batch_created_timestamp, batch_source, batch_purpose)
values
  (-10, 'report-tests.txt', '2011-01-01 03:00:00', 'DFT', 'PRINT') -- outgoing
, (-11, 'Rejected20990101.xml', '2010-01-01 01:01:00', 'PRINTER', 'REJECTED') --incoming rejected
, (-12, 'Production20990101.xml', '2010-01-01 01:01:00', 'PRINTER', 'ISSUED') --incoming issued
, (-13, 'report-tests2.txt', '2011-01-01 03:00:00', 'DFT', 'PRINT') -- outgoing
, (-14, 'Production20990102.xml', '2010-01-01 01:01:00', 'PRINTER', 'ISSUED'); --incoming issued


insert into badgemanagement.batch_badge
  (batch_id, badge_no, local_authority_short_code, issued_date_time, rejected_reason)
values
  -- printed
  (-10, 'KAKKKA', 'TEST_1', null, null)
  ,(-10, 'KAKKKB', 'TEST_2', null, null)
  ,(-10, 'KAKKKC', 'TEST_1', null, null)
  ,(-10, 'KAKKKD', 'TEST_2', null, null)

  -- rejected badges
  ,(-11, 'KAKKKA', null, null, 'Invalid badge')

  -- issued badges
  ,(-12, 'KAKKKB', null, '2019-02-01 00:00:00', null)
  ,(-12, 'KAKKKC', null, '2019-02-01 00:00:00', null)
  ,(-12, 'KAKKKD', null, '2019-02-01 01:00:00', null)

  -- print run 2
  ,(-13, 'KAKKKE', 'TEST_4', null, null)
  ,(-13, 'KAKKKK', 'TEST_1', null, null)

  -- issued badges from print run 2
  ,(-14, 'KAKKKE', null, '2019-03-01 00:00:00', null)
  ,(-14, 'KAKKKK', null, '2019-03-01 00:00:00', null)
;
