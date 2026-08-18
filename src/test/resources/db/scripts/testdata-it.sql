-- Insert access groups
INSERT INTO access_group (id, municipality_id, namespace, group_id)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '2281', 'NAMESPACE-1', '11111111-1111-1111-1111-111111111111'),
       ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '2281', 'NAMESPACE-1', '22222222-2222-2222-2222-222222222222');

-- Insert access users
INSERT INTO access_user (id, municipality_id, namespace, user_id)
VALUES ('33333333-3333-3333-3333-333333333333', '2281', 'NAMESPACE-1', 'joe01doe');

-- Insert access types for groups
INSERT INTO access_type (id, type, access_group_id)
VALUES ('at-001', 'label', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
       ('at-002', 'label', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
       ('at-003', 'label', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb');

-- Insert access types for users
INSERT INTO access_type (id, type, access_user_id)
VALUES ('at-004', 'label', '33333333-3333-3333-3333-333333333333');

-- Insert access records
INSERT INTO access (id, access_type_id, pattern, access_level)
VALUES ('acc-001', 'at-001', 'FA/K1/T1', 'R'),
       ('acc-002', 'at-001', 'FA/K2/**', 'LR'),
       ('acc-003', 'at-002', 'FA/**', 'RW'),
       ('acc-004', 'at-003', 'FK/**', 'RW'),
       ('acc-005', 'at-004', 'USER/**', 'RW');