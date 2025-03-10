CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMPTZ         NOT NULL,
    updated_at TIMESTAMPTZ,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(60)         NOT NULL,
    profile_id UUID
);

CREATE TABLE binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMPTZ  NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    bytes        BYTEA        NOT NULL
);

CREATE TABLE user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     TIMESTAMPTZ NOT NULL,
    updated_at     TIMESTAMPTZ,
    user_id        UUID UNIQUE NOT NULL,
    last_active_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE channels
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10) NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    content    TEXT,
    channel_id UUID        NOT NULL,
    author_id  UUID
);

CREATE TABLE read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMPTZ NOT NULL,
    updated_at   TIMESTAMPTZ,
    user_id      UUID UNIQUE NOT NULL,
    channel_id   UUID UNIQUE NOT NULL,
    last_read_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE message_attachments
(
    message_id    UUID NOT NULL,
    attachment_id UUID NOT NULL
);

-- Adding Foreign Key constraints

ALTER TABLE users
    ADD CONSTRAINT fk_users_profile_id
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents (id) ON DELETE CASCADE;

ALTER TABLE message_attachments
    ADD CONSTRAINT fk_message_attachments_attachment_id
        FOREIGN KEY (attachment_id)
            REFERENCES binary_contents (id);

ALTER TABLE message_attachments
    ADD CONSTRAINT fk_message_attachments_message_id
        FOREIGN KEY (message_id)
            REFERENCES messages (id);

ALTER TABLE user_statuses
    ADD CONSTRAINT fk_user_statuses_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_statuses_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_statuses_channel_id
        FOREIGN KEY (channel_id)
            REFERENCES channels (id) ON DELETE CASCADE;

ALTER TABLE messages
    ADD CONSTRAINT fk_messages_channel_id
        FOREIGN KEY (channel_id)
            REFERENCES channels (id) ON DELETE CASCADE;

ALTER TABLE messages
    ADD CONSTRAINT fk_messages_author_id
        FOREIGN KEY (author_id)
            REFERENCES users (id) ON DELETE SET NULL;

