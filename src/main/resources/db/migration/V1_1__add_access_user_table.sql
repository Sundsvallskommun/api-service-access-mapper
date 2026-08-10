create table if not exists access_user
(
    id              varchar(255) not null,
    municipality_id varchar(255),
    namespace       varchar(255),
    user_id         varchar(255),
    primary key (id)
) engine = InnoDB;

alter table if exists access_type
    add column if not exists access_user_id varchar(255);

alter table if exists access_type
    add constraint fk_access_user_id
        foreign key if not exists (access_user_id)
            references access_user (id);