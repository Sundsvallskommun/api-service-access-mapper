alter table access_group add column if not exists group_id varchar(255);

update access_group set group_id = id where group_id is null;

alter table access_group add constraint uk_municipality_id_namespace_group_id unique (municipality_id, namespace, group_id);