
CREATE TABLE users
(
    id         UUID PRIMARY KEY ,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    username   VARCHAR(50) UNIQUE          NOT NULL,
    email      VARCHAR(100) UNIQUE         NOT NULL,
    password   VARCHAR(60)                 NOT NULL,
    profile_id UUID,
);

CREATE TABLE binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    file_name    VARCHAR(255)                NOT NULL,
    size         BIGINT                      NOT NULL,
    content_type VARCHAR(100)                NOT NULL,
);

CREATE TABLE user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    user_id        UUID UNIQUE                 NOT NULL,
    last_active_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
);


CREATE TABLE channels
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(255)                NOT NULL,
);

CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    content    TEXT,
    channel_id UUID                        NOT NULL,
    author_id  UUID,
);

-- Message.attachments
CREATE TABLE message_attachments
(
    message_id    UUID,
    attachment_id UUID,
    PRIMARY KEY (message_id, attachment_id)
);

CREATE TABLE read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    user_id      UUID                        NOT NULL,
    channel_id   UUID                        NOT NULL,
    last_read_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    UNIQUE (user_id, channel_id)
);



-- 제약 조건
-- User (1) -> BinaryContent (1)
ALTER TABLE users
    ADD CONSTRAINT fk_user_binary_content
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents (id)
            ON DELETE SET NULL;

-- UserStatus (1) -> User (1)
ALTER TABLE user_statuses
    ADD CONSTRAINT fk_user_status_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;

-- Message (N) -> Channel (1)
ALTER TABLE messages
    ADD CONSTRAINT fk_message_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE;

-- Message (N) -> Author (1)
ALTER TABLE messages
    ADD CONSTRAINT fk_message_user
        FOREIGN KEY (author_id)
            REFERENCES users (id)
            ON DELETE SET NULL;

-- MessageAttachment (1) -> BinaryContent (1)
ALTER TABLE message_attachments
    ADD CONSTRAINT fk_message_attachment_binary_content
        FOREIGN KEY (attachment_id)
            REFERENCES binary_contents (id)
            ON DELETE CASCADE;

-- ReadStatus (N) -> User (1)
ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_status_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;

-- ReadStatus (N) -> User (1)
ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_status_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE;

