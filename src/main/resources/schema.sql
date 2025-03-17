
create table binary_content
(
    content_type varchar(255),
    file_name    varchar(255),
    path         varchar(255),
    size         bigint,
    usings       uuid,
    id           uuid not null
        primary key
        constraint fknevh0cbuo9bwo88tel97rcurb
            references base_entity
);

alter table binary_content
    owner to postgres;

create table channel
(
    description varchar(255),
    name        varchar(255),
    type        smallint
        constraint channel_type_check
            check ((type >= 0) AND (type <= 1)),
    id          uuid not null
        primary key
        constraint fkifoh6blllh1wghf1qwrf4upak
            references base_update_entity
);

alter table channel
    owner to postgres;

create table message
(
    author     uuid,
    channel_id uuid,
    content    varchar(255),
    id         uuid not null
        primary key
        constraint fkw42ponrts93dcod2uqtsnx87
            references base_update_entity
);

alter table message
    owner to postgres;

create table message_attachments
(
    binary_content_id uuid not null
        constraint fk77ournn4eglm1eg8aa51lks22
            references message,
    attachment_uuid   uuid
);

alter table message_attachments
    owner to postgres;

create table read_status
(
    channel_id   uuid,
    last_read_at timestamp(6) with time zone,
    user_id      uuid,
    id           uuid not null
        primary key
        constraint fk9a45sm1wckradlgww237lt19u
            references base_update_entity
);

alter table read_status
    owner to postgres;

create table user_status
(
    last_active_at timestamp(6) with time zone,
    user_id        uuid,
    id             uuid not null
        primary key
        constraint fk47ka3va4bwohwta6iyypy2ya4
            references base_update_entity
);

alter table user_status
    owner to postgres;

create table users
(
    email         varchar(255),
    password      varchar(255),
    profile_image uuid,
    username      varchar(255),
    id            uuid not null
        primary key
        constraint fkhvamjcd5c19e69hquqcxhbtpi
            references base_update_entity
);

alter table users
    owner to postgres;
