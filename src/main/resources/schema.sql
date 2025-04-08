create table binary_contents
(
    id           uuid primary key,
    created_at   timestamp with time zone not null,
    file_name    varchar(255)             not null,
    size         bigint                   not null,
    content_type varchar(100)             not null
    --bytes        bytea        not null
);

create table users
(
    id         uuid primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone,
    username   varchar(50)              not null
        constraint always_have_username unique,
    email      varchar(100)             not null
        constraint always_have_email unique,
    password   varchar(60)              not null,
    profile_id uuid references binary_contents on delete cascade
);

create table user_statuses
(
    id             uuid primary key,
    created_at     timestamp with time zone not null,
    updated_at     timestamp with time zone,
    user_id        uuid                     not null unique references users on delete cascade,
    last_active_at timestamp with time zone not null
);

create table channels
(
    id          uuid primary key,
    created_at  timestamp with time zone not null,
    updated_at  timestamp with time zone,
    name        varchar(100),
    description varchar(500),
    type        varchar(10)              not null check (type in ('PUBLIC', 'PRIVATE'))
);

create table read_statuses
(
    id           uuid primary key,
    created_at   timestamp with time zone not null,
    updated_at   timestamp with time zone,
    last_read_at timestamp with time zone,
    user_id      uuid references users on delete cascade,
    channel_id   uuid references channels on delete cascade,
    constraint uk_read_statuses unique (user_id, channel_id)
);

create table messages
(
    id         uuid primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone,
    content    text,
    channel_id uuid                     not null references channels on delete cascade,
    author_id  uuid                     references users on delete set null
);

create table message_attachments
(
    message_id    uuid references messages on delete cascade,
    attachment_id uuid references binary_contents on delete cascade
);



