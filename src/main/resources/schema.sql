create table if not exists binary_contents
(
    id            uuid                     default gen_random_uuid()            not null
        primary key,
    file_name     varchar(255)                                                  not null,
    size          integer                                                       not null
        constraint binary_contents_size_check
            check (size >= 0),
    content_type  varchar(50)                                                   not null,
    created_at    timestamp with time zone default now()                        not null,
    upload_status varchar(15)              default 'WAITING'::character varying not null
);

create table if not exists users
(
    id                    uuid                     default gen_random_uuid()         not null
        primary key,
    username              varchar(50)                                                not null
        unique,
    nickname              varchar(50)                                                not null,
    email                 varchar(100)                                               not null
        unique,
    password              varchar(200)                                               not null,
    phone_num             varchar(20)                                                not null,
    role                  varchar(20)              default 'USER'::character varying not null,
    phone_region          varchar(10)              default 'KR'::character varying   not null,
    is_active             boolean                  default true                      not null,
    introduce             text,
    profile_id            uuid
        constraint fk_profile
            references binary_contents
            on delete set null,
    created_at            timestamp with time zone default now()                     not null,
    updated_at            timestamp with time zone default now(),
    is_account_non_locked boolean                  default true
);

create table if not exists user_statuses
(
    id             uuid                     default gen_random_uuid() not null
        primary key,
    user_id        uuid                                               not null
        unique
        constraint fk_user
            references users
            on delete cascade,
    last_active_at timestamp with time zone default now()             not null,
    created_at     timestamp with time zone default now()             not null,
    updated_at     timestamp with time zone default now()
);

create table if not exists channels
(
    id           uuid                     default gen_random_uuid() not null
        primary key,
    owner_id     uuid                                               not null
        constraint fk_owner
            references users
            on delete set null,
    name         varchar(100)             default 'Unnamed Channel'::character varying,
    description  text,
    channel_type varchar(10)                                        not null
        constraint channels_channel_type_check
            check ((channel_type)::text = ANY
                   ((ARRAY ['PUBLIC'::character varying, 'PRIVATE'::character varying])::text[])),
    created_at   timestamp with time zone default now()             not null,
    updated_at   timestamp with time zone default now()
);

create table if not exists messages
(
    id         uuid                     default gen_random_uuid() not null
        primary key,
    channel_id uuid                                               not null
        constraint fk_channel
            references channels
            on delete cascade,
    author_id  uuid                                               not null
        constraint fk_author
            references users
            on delete set null,
    content    text                                               not null,
    created_at timestamp with time zone default now()             not null,
    updated_at timestamp with time zone default now()
);

create table if not exists read_statuses
(
    id                   uuid                     default gen_random_uuid() not null
        primary key,
    channel_id           uuid                                               not null
        constraint fk_channel
            references channels
            on delete cascade,
    user_id              uuid                                               not null
        constraint fk_user
            references users
            on delete cascade,
    last_read_at         timestamp with time zone default now()             not null,
    created_at           timestamp with time zone default now()             not null,
    updated_at           timestamp with time zone default now(),
    notification_enabled boolean                  default true,
    constraint unique_user_channel
        unique (user_id, channel_id)
);

create table if not exists message_attachments
(
    id            uuid                     default gen_random_uuid() not null
        primary key,
    message_id    uuid                                               not null
        constraint fk_message
            references messages
            on delete cascade,
    attachment_id uuid                                               not null
        constraint fk_attachment_file
            references binary_contents
            on delete cascade,
    created_at    timestamp with time zone default now()             not null
);

create table if not exists channel_members
(
    id         uuid                     default gen_random_uuid() not null
        primary key,
    member_id  uuid                                               not null
        constraint fk_member
            references users
            on delete cascade,
    channel_id uuid                                               not null
        constraint fk_channel
            references channels
            on delete cascade,
    created_at timestamp with time zone default now()             not null
);

create table if not exists persistent_logins
(
    username  varchar(64) not null,
    series    varchar(64) not null
        primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);

create table if not exists jwt_session
(
    id            uuid default gen_random_uuid() not null
        primary key,
    username      varchar(255)                   not null
        unique,
    access_token  varchar(2048)                  not null,
    refresh_token varchar(2048)                  not null,
    issued_at     timestamp,
    expires_at    timestamp
);

create table if not exists async_task_failure
(
    id             uuid         not null
        primary key,
    task_name      varchar(100) not null,
    request_id     varchar(40)  not null,
    failure_reason varchar(500) not null,
    failed_at      timestamp with time zone
);

create table if not exists notification
(
    id                uuid                     default gen_random_uuid() not null
        primary key,
    receiver_id       uuid                                               not null,
    title             varchar(100)                                       not null,
    content           varchar(255)                                       not null,
    notification_type varchar(20)                                        not null,
    target_id         uuid,
    created_at        timestamp with time zone default now()             not null
);
