drop table if exists read_statuses cascade;

drop table if exists user_statuses cascade;

drop table if exists message_attachments cascade;

drop table if exists messages cascade;

drop table if exists users cascade;

drop table if exists binary_contents cascade;

drop table if exists channels cascade;

CREATE TABLE binary_contents
(
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    content_type VARCHAR(100) NOT NULL
);

CREATE TABLE users
(
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
	username VARCHAR(50) NOT NULL UNIQUE,
	email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    profile_id UUID
);

ALTER TABLE users
ADD CONSTRAINT fk_binary_contents
FOREIGN KEY (profile_id)
REFERENCES binary_contents(id)
ON DELETE SET NULL;

CREATE TABLE channels
(
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    name VARCHAR(100),
    description VARCHAR(500),
    type VARCHAR(10) NOT NULL
);

ALTER TABLE channels
ADD CONSTRAINT channels_type_check
CHECK ( type IN ('PUBLIC', 'PRIVATE'));

CREATE TABLE messages
(
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    content TEXT,
    channel_id UUID NOT NULL,
    author_id UUID
);

ALTER TABLE messages
ADD CONSTRAINT fk_channels
FOREIGN KEY (channel_id)
REFERENCES channels(id)
ON DELETE CASCADE;

ALTER TABLE messages
ADD CONSTRAINT fk_users
FOREIGN KEY (author_id)
REFERENCES users(id)
ON DELETE SET NULL;

CREATE TABLE read_statuses
(
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    user_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    last_read_at TIMESTAMPTZ NOT NULL
);

ALTER TABLE read_statuses
ADD CONSTRAINT fk_read_statuses_users
FOREIGN KEY (user_id)
REFERENCES users(id)
ON DELETE CASCADE;

ALTER TABLE read_statuses
ADD CONSTRAINT fk_read_statuses_channels
FOREIGN KEY (channel_id)
REFERENCES channels(id)
ON DELETE CASCADE;

ALTER TABLE read_statuses
ADD CONSTRAINT uk_user_channel
UNIQUE (user_id, channel_id);

CREATE TABLE user_statuses
(
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    user_id UUID NOT NULL UNIQUE,
    last_active_at TIMESTAMPTZ NOT NULL
);

ALTER TABLE user_statuses
ADD CONSTRAINT fk_users
FOREIGN KEY (user_id)
REFERENCES users(id)
ON DELETE CASCADE;

CREATE TABLE message_attachments
(
    message_id UUID,
    attachment_id UUID
);

ALTER TABLE message_attachments
ADD CONSTRAINT fk_messages
FOREIGN KEY (message_id)
REFERENCES messages(id)
ON DELETE CASCADE;

ALTER TABLE message_attachments
ADD CONSTRAINT fk_binary_contents
FOREIGN KEY (attachment_id)
REFERENCES binary_contents(id)
ON DELETE CASCADE;