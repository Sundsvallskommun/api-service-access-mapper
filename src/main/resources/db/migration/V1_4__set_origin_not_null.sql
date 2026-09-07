update access_user set origin = 'MANUAL' where origin is null;

alter table access_user modify column origin varchar(30) not null;