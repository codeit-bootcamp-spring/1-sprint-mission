-- schema-h2.sql 🐣 테스트 전용 H2용 스키마야! 💕

-- User
CREATE TABLE users
(
    id         VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP           NOT NULL,
    updated_at TIMESTAMP,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(60)         NOT NULL,
    profile_id VARCHAR(36)
);

-- BinaryContent
CREATE TABLE binary_contents
(
    id           VARCHAR(36) PRIMARY KEY,
    created_at   TIMESTAMP    NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    bytes        VARBINARY    NOT NULL
);

-- UserStatus
CREATE TABLE user_statuses
(
    id             VARCHAR(36) PRIMARY KEY,
    created_at     TIMESTAMP          NOT NULL,
    updated_at     TIMESTAMP,
    user_id        VARCHAR(36) UNIQUE NOT NULL,
    last_active_at TIMESTAMP          NOT NULL
);

-- Channel
CREATE TABLE channels
(
    id          VARCHAR(36) PRIMARY KEY,
    created_at  TIMESTAMP   NOT NULL,
    updated_at  TIMESTAMP,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10) NOT NULL
);

-- Message
CREATE TABLE messages
(
    id         VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP   NOT NULL,
    updated_at TIMESTAMP,
    content    TEXT,
    channel_id VARCHAR(36) NOT NULL,
    author_id  VARCHAR(36)
);

-- Message.attachments
CREATE TABLE message_attachments
(
    message_id    VARCHAR(36),
    attachment_id VARCHAR(36),
    PRIMARY KEY (message_id, attachment_id)
);

-- ReadStatus
CREATE TABLE read_statuses
(
    id           VARCHAR(36) PRIMARY KEY,
    created_at   TIMESTAMP   NOT NULL,
    updated_at   TIMESTAMP,
    user_id      VARCHAR(36) NOT NULL,
    channel_id   VARCHAR(36) NOT NULL,
    last_read_at TIMESTAMP   NOT NULL,
    UNIQUE (user_id, channel_id)
);

-- FK 설정들
ALTER TABLE users
    ADD CONSTRAINT fk_user_binary_content
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents (id)
            ON DELETE SET NULL;

ALTER TABLE user_statuses
    ADD CONSTRAINT fk_user_status_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;

ALTER TABLE messages
    ADD CONSTRAINT fk_message_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE;

ALTER TABLE messages
    ADD CONSTRAINT fk_message_user
        FOREIGN KEY (author_id)
            REFERENCES users (id)
            ON DELETE SET NULL;

ALTER TABLE message_attachments
    ADD CONSTRAINT fk_message_attachment_binary_content
        FOREIGN KEY (attachment_id)
            REFERENCES binary_contents (id)
            ON DELETE CASCADE;

ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_status_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;

ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_status_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE;
