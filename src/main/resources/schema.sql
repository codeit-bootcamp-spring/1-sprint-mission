-- 테이블 생성
-- 유저 테이블 생성
CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    username   VARCHAR(50) UNIQUE       NOT NULL,
    email      VARCHAR(100) UNIQUE      NOT NULL,
    password   VARCHAR(60)              NOT NULL,
    profile_id UUID,
    role       VARCHAR(30)              NOT NULL
);

-- 채널 테이블 생성
CREATE TABLE channels
(
    id          UUID PRIMARY KEY,
    created_at  timestamp with time zone NOT NULL,
    updated_at  timestamp with time zone,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10)              NOT NULL
);

-- 채널 멤버 테이블 생성
CREATE TABLE channel_members
(
    channel_id UUID,
    user_id    UUID
);

-- 메시지 테이블 생성
CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    content    TEXT,
    channel_id UUID                     NOT NULL,
    author_id  UUID
);

-- 메시지 첨부파일 테이블 생성
CREATE TABLE message_attachments
(
    message_id    UUID,
    attachment_id UUID,
    PRIMARY KEY (message_id, attachment_id)
);

-- 이미지 파일 테이블 생성
CREATE TABLE binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   timestamp with time zone NOT NULL,
    file_name    VARCHAR(255)             NOT NULL,
    size         BIGINT                   NOT NULL,
    content_type VARCHAR(100)             NOT NULL
);

-- 유저 상태 테이블 생성
CREATE TABLE user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     timestamp with time zone NOT NULL,
    updated_at     timestamp with time zone,
    user_id        UUID UNIQUE              NOT NULL,
    last_active_at timestamp with time zone NOT NULL
);

-- 메시지 읽음 상태 테이블 생성
CREATE TABLE read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   timestamp with time zone NOT NULL,
    updated_at   timestamp with time zone,
    user_id      UUID                     NOT NULL,
    channel_id   UUID                     NOT NULL,
    last_read_at timestamp with time zone NOT NULL,
    UNIQUE (user_id, channel_id)
);


-- 제약조건 설정
-- User -> BinaryContent (1:1)
ALTER TABLE users
    ADD CONSTRAINT fk_user_binary_content
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents (id)
            ON DELETE SET NULL;

-- UserStatus -> User (1:1)
ALTER TABLE user_statuses
    ADD CONSTRAINT fk_user_status_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;

-- Message -> Channel (N:1)
ALTER TABLE messages
    ADD CONSTRAINT fk_message_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE;

-- Message -> Author (N:1)
ALTER TABLE messages
    ADD CONSTRAINT fk_message_user
        FOREIGN KEY (author_id)
            REFERENCES users (id)
            ON DELETE SET NULL;

-- MessageAttachment -> BinaryContent (1:1)
ALTER TABLE message_attachments
    ADD CONSTRAINT fk_message_attachment_binary_content
        FOREIGN KEY (attachment_id)
            REFERENCES binary_contents (id)
            ON DELETE CASCADE;

-- ReadStatus -> User (N:1)
ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_status_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;

-- ReadStatus -> Channel (N:1)
ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_status_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE;