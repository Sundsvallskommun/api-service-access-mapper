
    create table access (
        access_level varchar(255),
        access_type_id varchar(255),
        id varchar(255) not null,
        pattern varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table access_group (
        id varchar(255) not null,
        municipality_id varchar(255),
        namespace varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table access_type (
        access_group_id varchar(255),
        id varchar(255) not null,
        type varchar(255),
        primary key (id)
    ) engine=InnoDB;

    alter table if exists access 
       add constraint fk_access_type_id 
       foreign key (access_type_id) 
       references access_type (id);

    alter table if exists access_type 
       add constraint fk_access_group_id 
       foreign key (access_group_id) 
       references access_group (id);
