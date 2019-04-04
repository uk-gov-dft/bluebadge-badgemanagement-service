delete from badgemanagement.batch_badge where batch_id=-3 and badge_no = 'NNNJMJ';
delete from badgemanagement.batch_badge where batch_id=-4 and badge_no = 'NNNJMH';
delete from badgemanagement.batch_badge where batch_id=-5 and badge_no = 'NNNJMH';
delete from badgemanagement.batch_badge where batch_id=-6 and badge_no = 'NNNJMF';
delete from badgemanagement.batch_badge where batch_id=-7 and badge_no = 'NNNJMF';


delete from badgemanagement.badge where badge_no in ('NNNJMJ', 'NNNJMH', 'NNNJMF');

delete from badgemanagement.batch where batch_id in (-1, -3, -4, -5, -6, -7);

insert into badgemanagement.badge (badge_no, badge_status, party_code,
        local_authority_short_code, local_authority_ref,
        app_channel_code, start_date, expiry_date,
        eligibility_code, image_link, image_link_original, deliver_to_code,
        deliver_option_code, holder_name, nino,
        dob, gender_code, contact_name,
        contact_building_street, contact_line2, contact_town_city,
        contact_postcode, primary_phone_no, secondary_phone_no,
        contact_email_address, holder_name_upper, order_date, app_date) values (
        'NNNJMJ', 'PROCESSED', 'PERSON', 'ABERD', 'sent printer date test', 'ONLINE', '2020-03-05', '2021-02-08', 'DLA', '', '','HOME', 'STAND', 'Sopoline Rasmussen', 'NS123456A',
        '1977-10-11', 'UNSPECIFIE', 'Miranda Clark', '19 South Milton Boulevard', '328 East Green Fabien Drive', '781 Nobel Avenue', 'M41FS', '07951546060', '07951546060', 'zode@mailinator.net', 'SOPOLINE RASMUSSEN', '2019-03-07', '2000-04-21')
        ,('NNNJMH', 'ISSUED', 'PERSON', 'ABERD', 'issued date test', 'ONLINE', '2020-11-25', '2021-10-10', 'AFRFCS', '', '', 'HOME', 'STAND', 'Guinevere Terrell', 'NS123456A',
         '1977-06-11', 'MALE', 'Hadassah Flowers', '55 North First Street', '131 North Oak Court', '24 Rocky Fabien Boulevard', 'UV665DQ', '07951546060', '07951546060', 'josotehe@mailinator.net', 'GUINEVERE TERRELL', '2019-03-07', '2000-05-20')
        ,('NNNJMF', 'REJECT', 'PERSON', 'ABERD', 'reject date test', 'ONLINE', '2020-09-19', '2022-12-02', 'ARMS', '', '', 'HOME', 'STAND', 'Pascale Velez', 'NS123456A',
        '2000-01-26', 'UNSPECIFIE', 'Odette Dyer', '51 Nobel Freeway', '128 South Clarendon Avenue', '45 West Rocky Second Boulevard', 'KP081PF', '07951546060', '07951546060', 'liduti@mailinator.com', 'PASCALE VELEZ', '2019-03-07', '1988-07-16')
       ;

insert into badgemanagement.batch (batch_id, batch_filename, batch_created_timestamp, batch_source, batch_purpose)
values (-3, 'printed.txt',       '2019-03-07 01:01:00', 'DFT', 'STANDARD')
, (-4, 'toBeIssued.txt',    '2019-03-07 01:01:00', 'DFT', 'STANDARD')
, (-5, 'issued.txt',        '2019-03-07 01:02:00', 'PRINTER', 'ISSUED')
, (-6, 'toBeRejected.txt',  '2019-03-07 01:03:00', 'DFT', 'STANDARD')
, (-7, 'rejected.txt',      '2019-03-07 01:04:00', 'PRINTER', 'REJECTED')
;

insert into badgemanagement.batch_badge (batch_id, badge_no, local_authority_short_code, issued_date_time, rejected_reason)
values (-3, 'NNNJMJ', 'ABERD', null, null)
, (-4, 'NNNJMH', 'ABERD', null, null)
, (-5, 'NNNJMH', null, '2019-03-07 01:02:00', null)
, (-6, 'NNNJMF', 'ABERD', null, null)
, (-7, 'NNNJMF', null, null, 'my rejected reason')
;
