-- binary_contents
create table binary_contents
(
    id           uuid primary key,
    created_at   timestamptz  not null,
    file_name    varchar(255) not null,
    size         bigint       not null,
    content_type varchar(100) not null,
    bytes        bytea        not null
);

-- users
create table users
(
    id         uuid primary key,
    created_at timestamptz  not null,
    updated_at timestamptz,
    username   varchar(50)  not null unique,
    email      varchar(100) not null unique,
    password   varchar(60)  not null,
    profile_id uuid
);
alter table users
    add constraint profile_id
        foreign key (profile_id) references binary_contents (id) on delete cascade;

-- user_statuses
create table user_statuses
(
    id             uuid primary key,
    created_at     timestamptz not null,
    updated_at     timestamptz,
    user_id        uuid        not null unique,
    last_active_at timestamptz not null
);
alter table user_statuses
    add constraint user_id
        foreign key (user_id) references users (id) on delete cascade;


-- channels
create table channels
(
    id          uuid primary key,
    created_at  timestamptz not null,
    updated_at  timestamptz,
    name        varchar(100),
    description varchar(500),
    type        varchar(10) not null
);

-- messages
create table messages
(
    id         uuid primary key,
    created_at timestamptz not null,
    updated_at timestamptz,
    content    text,
    channel_id uuid        not null,
    author_id  uuid
);
alter table messages
    add constraint channel_id
        foreign key (channel_id) references channels (id) on delete cascade;
alter table messages
    add constraint author_id
        foreign key (author_id) references users (id) on delete set null;

-- read_statuses
create table read_statuses
(
    id           uuid primary key,
    created_at   timestamptz not null,
    updated_at   timestamptz,
    user_id      uuid,
    channel_id   uuid,
    last_read_at timestamptz not null
);
alter table read_statuses
    add unique (user_id, channel_id);
alter table read_statuses
    add constraint user_id
        foreign key (user_id) references users (id) on delete cascade;
alter table read_statuses
    add constraint channel_id
        foreign key (channel_id) references channels (id) on delete cascade;

-- message_attachments
create table message_attachments
(
    message_id    uuid,
    attachment_id uuid
);
alter table message_attachments
    add constraint message_id
        foreign key (message_id) references messages (id) on delete cascade;
alter table message_attachments
    add constraint attachment_id
        foreign key (attachment_id) references binary_contents (id) on delete cascade;