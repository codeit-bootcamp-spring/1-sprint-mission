CREATE USER discodeit_user PASSWORD 'discodeit1234';
CREATE DATABASE discodeit WITH OWNER discodeit_user;

GRANT ALL PRIVILEGES ON DATABASE discodeit TO discodeit_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO discodeit_user;

\c discodeit;
GRANT USAGE ON SCHEMA public TO discodeit_user;
GRANT CREATE ON SCHEMA public TO discodeit_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO discodeit_user;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO discodeit_user;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO discodeit_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO discodeit_user;

create table binary_contents(
    id uuid primary key,
    created_at timestamp not null,
    file_name varchar(255) not null,
    size bigint not null,
    content_type varchar(100) not null
);

create table users(
    id uuid primary key,
    created_at timestamp not null,
    updated_at timestamp,
    username varchar(50) not null unique,
    email varchar(100) not null unique,
    password varchar(60) not null,
    profile_id uuid references binary_contents(id) on delete set null
);

create table user_statuses(
    id uuid primary key,
    created_at timestamp not null,
    updated_at timestamp,
    user_id uuid not null unique references users(id) on delete cascade,
    last_active_at timestamp not null
);

create type channel_type as enum('PRIVATE', 'PUBLIC');

create table channels(
    id uuid primary key,
    created_at timestamp not null,
    updated_at timestamp,
    name varchar(100),
    description varchar(500),
    type channel_type not null
);

create table read_statuses(
    id uuid primary key,
    created_at timestamp not null,
    updated_at timestamp,
    user_id uuid references users(id) on delete cascade,
    channel_id uuid references channels(id) on delete cascade,
    last_read_at timestamp not null,
    constraint read_statuses_user_id_channel_id_key UNIQUE (user_id, channel_id)
);

create table messages(
    id uuid primary key,
    created_at timestamp not null,
    updated_at timestamp,
    content text,
    channel_id uuid not null references channels(id) on delete cascade,
    author_id uuid references users(id) on delete set null
);

create table message_attachments(
    message_id uuid references messages(id) on delete cascade,
    attachment_id uuid references binary_contents(id) on delete cascade,
    primary key (message_id, attachment_id)
);