-- H2 Test Data for Address Lookup API
INSERT INTO addresses (id, uprn, udprn, uprn_match_type, premises, address_line1, address_line_2, post_town, country, postcode, is_welsh, is_residential, classicat_code, classicat_short_description, classicat_full_description, latitude, longitude)
VALUES 
(1, 100001, 50001, 'match', '10', 'Downing Street', '', 'London', 'England', 'SW1A 1AA', false, true, 'RH05', 'Royal Household', 'Royal Household', 51.5033, -0.1276),
(2, 100002, 50002, 'match', '10', 'Downing Street', 'Annexe', 'London', 'England', 'SW1A 1AA', false, false, 'OP27', 'Central Government Office', 'Central Government Office', 51.5034, -0.1275),
(3, 100003, 50003, 'match', '70', 'Whitehall', '', 'London', 'England', 'SW1A 2AA', false, false, 'OP27', 'Central Government Office', 'Central Government Office', 51.5035, -0.1265),
(4, 100004, 50004, 'match', 'Tower 42', '25 Old Broad Street', '', 'London', 'England', 'EC2N 1HQ', false, false, 'CO', 'Commercial', 'Commercial', 51.5140, -0.0857),
(5, 100005, 50005, 'match', '', 'Abbey Road', '', 'London', 'England', 'NW8 9AY', false, false, 'CMP', 'Unclassified Commercial', 'Unclassified Commercial', 51.5358, -0.1821);
