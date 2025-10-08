-- Insert access groups
INSERT INTO access_group (id, municipality_id, namespace)
VALUES ('11111111-1111-1111-1111-111111111111', '2281', 'NAMESPACE-1'),
       ('22222222-2222-2222-2222-222222222222', '2281', 'NAMESPACE-1');

-- Insert access types
INSERT INTO access_type (id, type, access_group_id)
VALUES ('at-001', 'label', '11111111-1111-1111-1111-111111111111'),
       ('at-002', 'label', '11111111-1111-1111-1111-111111111111'),
       ('at-003', 'label', '22222222-2222-2222-2222-222222222222');

-- Insert access records
INSERT INTO access (id, access_type_id, pattern, access_level)
VALUES ('acc-001', 'at-001', 'FA/K1/T1', 'R'),
       ('acc-002', 'at-001', 'FA/K2/**', 'LR'),
       ('acc-003', 'at-002', 'FA/**', 'RW'),
       ('acc-004', 'at-003', 'FK/**', 'RW');
