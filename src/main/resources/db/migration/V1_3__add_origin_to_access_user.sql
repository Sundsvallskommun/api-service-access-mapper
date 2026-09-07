alter table if exists access_user
    add column if not exists origin varchar(255);