-- binary_contents
create table binary_contents
(
    id           uuid primary key,
    created_at   timestamp with time zone not null,
    file_name    varchar(255)             not null,
    size         bigint                   not null,
    content_type varchar(100)             not null
--     bytes        bytea                    not null
);
-- alter table binary_contents
--     drop bytes;

-- users
create table users
(
    id         uuid primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone,
    username   varchar(50)              not null unique,
    email      varchar(100)             not null unique,
    password   varchar(60)              not null,
    profile_id uuid
);
alter table users
    add constraint fk_user_profile_id
        foreign key (profile_id) references binary_contents (id) on delete cascade;

-- user_statuses
create table user_statuses
(
    id             uuid primary key,
    created_at     timestamp with time zone not null,
    updated_at     timestamp with time zone,
    user_id        uuid                     not null unique,
    last_active_at timestamp with time zone not null
);
alter table user_statuses
    add constraint fk_user_status_user_id
        foreign key (user_id) references users (id) on delete cascade;


-- channels
create table channels
(
    id          uuid primary key,
    created_at  timestamp with time zone not null,
    updated_at  timestamp with time zone,
    name        varchar(100),
    description varchar(500),
    type        varchar(10)              not null
);

-- messages
create table messages
(
    id         uuid primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone,
    content    text,
    channel_id uuid                     not null,
    author_id  uuid
);
alter table messages
    add constraint fk_message_channel_id
        foreign key (channel_id) references channels (id) on delete cascade;
alter table messages
    add constraint fk_message_author_id
        foreign key (author_id) references users (id) on delete set null;

-- read_statuses
create table read_statuses
(
    id           uuid primary key,
    created_at   timestamp with time zone not null,
    updated_at   timestamp with time zone,
    user_id      uuid,
    channel_id   uuid,
    last_read_at timestamp with time zone not null
);
alter table read_statuses
    add unique (user_id, channel_id);
alter table read_statuses
    add constraint fk_read_status_user_id
        foreign key (user_id) references users (id) on delete cascade;
alter table read_statuses
    add constraint fk_read_status_channel_id
        foreign key (channel_id) references channels (id) on delete cascade;

-- message_attachments
create table message_attachments
(
    message_id    uuid,
    attachment_id uuid
);
alter table message_attachments
    add constraint fk_message_attachments_message_id
        foreign key (message_id) references messages (id) on delete cascade;
alter table message_attachments
    add constraint fk_message_attachments_attachment_id
        foreign key (attachment_id) references binary_contents (id) on delete cascade;