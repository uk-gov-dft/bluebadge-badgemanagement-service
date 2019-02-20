delete from badgemanagement.batch where batch_id in (-10, -11, -12);

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
        'KAKKKK', 'ISSUED', 'PERSON', 'ABERD', 'to delete', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', 'badge/KKKKKK/thumbnail.jpg', 'badge/KKKKKK/original.jpg', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637FU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KAKKKD', 'ISSUED', 'PERSON', 'ABERD', 'to retrieve', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637DU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KAKKKA', 'ORDERED', 'PERSON', 'ABERD', 'to update', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KAKKKC', 'DELETED', 'PERSON', 'ABERD', 'deleted', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637FU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KAKKKE', 'ORDERED', 'PERSON', 'ABERD', 'to find badge for print batch', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'FAST', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test106@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KAKKKB', 'ORDERED', 'PERSON', 'ABERD', 'to find badge for print batch', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
;

insert into badgemanagement.batch (batch_id, batch_filename, batch_created_timestamp, batch_source, batch_purpose)
values (-10, 'report-tests.txt', '2011-01-01 03:00:00', 'DFT', 'STANDARD')
, (-11, 'report-tests-2.txt', '2010-01-01 01:01:00', 'DFT', 'FASTTRACK')
, (-12, 'report-tests-3.txt', '2010-01-01 01:01:00', 'DFT', 'FASTTRACK')
;

insert into badgemanagement.batch_badge
  (batch_id, badge_no, local_authority_short_code, issued_date_time)
values
  (-10, 'KAKKKK', 'TEST_1', '2019-02-01 05:12:23')
  ,(-11, 'KAKKKD', 'TEST_2', '2019-02-02 20:12:23')
  ,(-11, 'KAKKKB', 'TEST_2', '2019-02-03 20:12:23')

  ,(-10, 'KAKKKA', 'TEST_1', null)
  ,(-11, 'KAKKKA', 'TEST_1', '2015-02-02 20:12:23')
  ,(-12, 'KAKKKA', 'TEST_1', '2100-02-02 20:12:23')

  ,(-11, 'KAKKKC', 'TEST_1', '2019-02-01 00:00:00')
  ,(-11, 'KAKKKE', 'TEST_4', '2019-02-05 00:00:00')
;
