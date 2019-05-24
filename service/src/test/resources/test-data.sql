delete from badgemanagement.batch_badge where batch_id < 0;

delete from badgemanagement.badge where badge_no in
('KKKKKK', 'KKKKKD', 'KKKKKA', 'KKKKKC', 'KKKKKE',
'KKKKKB', 'KKKKDA', 'KKKKDB', 'KKKKDC', 'NNNJMJ',
'NNNJMH', 'NNNJMF', 'FINDA1', 'FINDA2', 'DUPES1');

delete from badgemanagement.batch where batch_id < 0;

insert into badgemanagement.badge (badge_no, badge_status, party_code,
        local_authority_short_code, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, not_for_reassessment, image_link, image_link_original, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date) values (
        'KKKKKK', 'ISSUED', 'PERSON', 'ABERD', 'to delete', 'ONLINE', '2025-05-01', '2028-05-01', 'CHILDBULK', true, 'badge/KKKKKK/thumbnail.jpg', 'badge/KKKKKK/original.jpg', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637FU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKKD', 'ISSUED', 'PERSON', 'ABERD', 'to retrieve', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', null, '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637DU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKKA', 'ORDERED', 'PERSON', 'ABERD', 'to update', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', null, '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKKC', 'DELETED', 'PERSON', 'ABERD', 'deleted', 'ONLINE', '2025-05-01', '2028-05-01', 'CHILDVEHIC', false, '', '', 'HOME', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637FU', '020 7014 0800', null, 'test101@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKKE', 'ORDERED', 'PERSON', 'ABERD', 'to find badge for print batch', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', null, '', '', 'HOME', 'FAST', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test106@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKKB', 'ORDERED', 'PERSON', 'ABERD', 'to find badge for print batch', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', null,'', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKDA', 'PROCESSED', 'PERSON', 'ABERD', 'to set issued', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', null,'', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKDB', 'PROCESSED', 'PERSON', 'ABERD', 'to set rejected', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', null, '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('KKKKDC', 'DELETED', 'PERSON', 'ABERD', 'to not update to issued', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', null, '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('NNNJMJ', 'PROCESSED', 'PERSON', 'ABERD', 'sent printer date test', 'ONLINE', '2020-03-05', '2021-02-08', 'DLA', null, '', '','HOME', 'STAND', 'Sopoline Rasmussen', 'NS123456A',
        '1977-10-11', 'UNSPECIFIE', 'Miranda Clark', '19 South Milton Boulevard', '328 East Green Fabien Drive', '781 Nobel Avenue', 'M41FS', '07951546060', '07951546060', 'zode@mailinator.net', 'SOPOLINE RASMUSSEN', '2019-03-07', '2000-04-21')
        ,('NNNJMH', 'ISSUED', 'PERSON', 'ABERD', 'issued date test', 'ONLINE', '2020-11-25', '2021-10-10', 'AFRFCS', null, '', '', 'HOME', 'STAND', 'Guinevere Terrell', 'NS123456A',
         '1977-06-11', 'MALE', 'Hadassah Flowers', '55 North First Street', '131 North Oak Court', '24 Rocky Fabien Boulevard', 'UV665DQ', '07951546060', '07951546060', 'josotehe@mailinator.net', 'GUINEVERE TERRELL', '2019-03-07', '2000-05-20')
        ,('NNNJMF', 'REJECT', 'PERSON', 'ABERD', 'reject date test', 'ONLINE', '2020-09-19', '2022-12-02', 'ARMS', null, '', '', 'HOME', 'STAND', 'Pascale Velez', 'NS123456A',
        '2000-01-26', 'UNSPECIFIE', 'Odette Dyer', '51 Nobel Freeway', '128 South Clarendon Avenue', '45 West Rocky Second Boulevard', 'KP081PF', '07951546060', '07951546060', 'liduti@mailinator.com', 'PASCALE VELEZ', '2019-03-07', '1988-07-16')
        ,('FINDA1', 'DELETED', 'PERSON', 'FINDBYLA', 'find badges by la', 'ONLINE', '2025-05-01', '2028-05-01', 'CHILDBULK', false, '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('FINDA2', 'ISSUED', 'PERSON', 'FINDBYLA', 'find badges by la', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', null, '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('FINDA3', 'ORDERED', 'PERSON', 'FINDBYLA', 'find badges by la', 'ONLINE', '2025-05-01', '2028-05-01', 'WALKD', true, '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('DUPES1', 'ISSUED', 'PERSON', 'DUPES', 'find badges by la', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
        ,('DUPES2', 'REJECT', 'PERSON', 'DUPES', 'find badges by la', 'ONLINE', '2025-05-01', '2028-05-01', 'PIP', '', '', 'COUNCIL', 'STAND', 'Reginald Pai', '',
        '1953-09-12', 'MALE', 'contact name', 'building and street', '', 'Town or city', 'S637EU', '020 7014 0800', null, 'test107@mailinator.com', 'REGINALD', '2018-07-24', ' 2018-06-01')
;

insert into badgemanagement.batch (batch_id, batch_filename, batch_created_timestamp, batch_source, batch_purpose)
values (-1, 'filename.txt', '2011-01-01 03:00:00', 'DFT', 'STANDARD')
, (-2, 'linkTest.txt', '2010-01-01 01:01:00', 'DFT', 'FASTTRACK')
, (-3, 'la_exportissuedTest.txt', '2010-02-02 01:01:00', 'PRINTER', 'ISSUED')
, (-4, 'laExportrejectedTest.txt', '2010-02-03 15:16:17', 'PRINTER', 'REJECTED')
, (-5, 'laExportPrint.txt', '2011-01-01 03:00:00', 'DFT', 'STANDARD')
, (-6, 'linkTest.txt',      '2010-01-01 01:01:00', 'DFT', 'FASTTRACK')
, (-7, 'printed.txt',       '2019-03-07 01:01:00', 'DFT', 'STANDARD')
, (-8, 'toBeIssued.txt',    '2019-03-07 01:01:00', 'DFT', 'STANDARD')
, (-9, 'issued.txt',        '2019-03-07 01:02:00', 'PRINTER', 'ISSUED')
, (-10, 'toBeRejected.txt', '2019-03-07 01:03:00', 'DFT', 'STANDARD')
, (-11, 'rejected.txt',     '2019-03-07 01:04:00', 'PRINTER', 'REJECTED')
, (-12, 'confirm_dupe.txt', '2019-03-07 01:04:00', 'PRINTER', 'ISSUED')
, (-13, 'confirm_dupe2.txt', '2019-03-07 01:04:00', 'PRINTER', 'ISSUED')
, (-14, 'reject_dupe.txt',  '2019-03-07 01:04:00', 'PRINTER', 'REJECTED')
, (-15, 'reject_dupe2.txt', '2019-03-07 01:05:00', 'PRINTER', 'REJECTED')
, (-16, 'filename.txt', '2011-01-01 03:00:00', 'DFT', 'STANDARD')
, (-17, 'linkTest.txt', '2010-01-01 01:01:00', 'DFT', 'FASTTRACK')

;

insert into badgemanagement.batch_badge (batch_id, badge_no, local_authority_short_code, issued_date_time, rejected_reason)
values (-1, 'KKKKKA', null, null, null)
, (-7, 'NNNJMJ', 'ABERD', null, null)
, (-8, 'NNNJMH', 'ABERD', null, null)
, (-9, 'NNNJMH', null, '2019-03-07 01:02:00', null)
, (-10, 'NNNJMF', 'ABERD', null, null)
, (-11, 'NNNJMF', null, null, 'my rejected reason')
, (-12, 'DUPES1', null, '2019-03-07 01:03:00', null)
, (-13, 'DUPES1', null, '2019-03-07 01:02:00', null)
, (-14, 'DUPES2', null, null, 'help')
, (-15, 'DUPES2', null, null, 'help')
, (-16, 'DUPES2', 'ABERD', null, null)
, (-17, 'DUPES2', 'ABERD', null, null)

;

insert into badgemanagement.batch_badge (batch_id, badge_no) values (-5, 'FINDA2');
insert into badgemanagement.batch_badge (batch_id, badge_no, issued_date_time) values (-3, 'FINDA2', current_timestamp);
insert into badgemanagement.batch_badge (batch_id, badge_no, rejected_reason) values (-4, 'FINDA2', 'rejected reason');
