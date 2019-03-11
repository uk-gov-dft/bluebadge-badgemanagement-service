delete from badgemanagement.batch_badge where batch_id=-1 and badge_no = 'KKKKKA';

delete from badgemanagement.badge where badge_no in ('KKKKKK', 'KKKKKD', 'KKKKKA', 'KKKKKC', 'KKKKKE', 'KKKKKB');

delete from badgemanagement.batch where batch_id = -1;

insert into badgemanagement.badge (badge_no, badge_status, party_code,
        local_authority_short_code, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, image_link, image_link_original, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date) values (
        'KKKKKK', 'ISSUED', 'PERSON', 'ABERD', 'to delete', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', 'badge/KKKKKK/thumbnail.jpg', 'badge/KKKKKK/original.jpg', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637FU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKKD', 'ISSUED', 'PERSON', 'ABERD', 'to retrieve', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637DU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKKA', 'ORDERED', 'PERSON', 'ABERD', 'to update', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKKC', 'DELETED', 'PERSON', 'ABERD', 'deleted', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637FU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKKE', 'ORDERED', 'PERSON', 'ABERD', 'to find badge for print batch', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'HOME', 'FAST', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test106@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKKB', 'ORDERED', 'PERSON', 'ABERD', 'to find badge for print batch', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKDA', 'PROCESSED', 'PERSON', 'ABERD', 'to set issued', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKDB', 'PROCESSED', 'PERSON', 'ABERD', 'to set rejected', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKDC', 'DELETED', 'PERSON', 'ABERD', 'to not update to issued', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('FINDA1', 'DELETED', 'PERSON', 'FINDBYLA', 'find badges by la', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('FINDA2', 'ISSUED', 'PERSON', 'FINDBYLA', 'find badges by la', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
;

insert into badgemanagement.batch (batch_id, batch_filename, batch_created_timestamp, batch_source, batch_purpose)
values (-1, 'filename.txt', '2011-01-01 03:00:00', 'DFT', 'STANDARD')
, (-2, 'linkTest.txt', '2010-01-01 01:01:00', 'DFT', 'FASTTRACK')
, (-3, 'la_exportissuedTest.txt', '2010-02-02 01:01:00', 'PRINTER', 'ISSUED')
, (-4, 'laExportrejectedTest.txt', '2010-02-03 15:16:17', 'PRINTER', 'REJECTED')
, (-5, 'laExportPrint.txt', '2011-01-01 03:00:00', 'DFT', 'STANDARD')
;

insert into badgemanagement.batch_badge (batch_id, badge_no) values (-1, 'KKKKKA');
insert into badgemanagement.batch_badge (batch_id, badge_no) values (-5, 'FINDA2');
insert into badgemanagement.batch_badge (batch_id, badge_no, issued_date_time) values (-3, 'FINDA2', current_timestamp);
insert into badgemanagement.batch_badge (batch_id, badge_no, rejected_reason) values (-4, 'FINDA2', 'rejected reason');
