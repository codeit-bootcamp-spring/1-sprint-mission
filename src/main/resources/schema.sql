-- << 테이블 생성 시작 >>
-- 유저 테이블 생성
CREATE TABLE users
(
    id           UUID,
    create_at    TIMESTAMPTZ,
    update_at    TIMESTAMPTZ,
    username     VARCHAR(50),
    email        VARCHAR(100),
    password     VARCHAR(60),
    nickname     VARCHAR(50),
    phone_number VARCHAR(30),
    profile_id   UUID
);

-- 채널 테이블 생성
CREATE TABLE channels
(
    id          UUID,
    create_at   TIMESTAMPTZ,
    update_at   TIMESTAMPTZ,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10),
    category    VARCHAR(200),
    owner_id    UUID
);

CREATE TABLE channel_members
(
    channel_id UUID,
    user_id    UUID
);

-- 메시지 테이블 생성
CREATE TABLE messages
(
    id         UUID,
    create_at  TIMESTAMPTZ,
    update_at  TIMESTAMPTZ,
    content    TEXT,
    channel_id UUID,
    author_id  UUID
);

-- 메시지 첨부파일 테이블 생성
CREATE TABLE message_attachments
(
    message_id    UUID,
    attachment_id UUID
);

-- 이미지 파일 테이블 생성
CREATE TABLE binary_contents
(
    id           UUID,
    create_at    TIMESTAMPTZ,
    file_name    VARCHAR(255),
    size         BIGINT,
    content_type VARCHAR(100),
    file_path    VARCHAR(500)
);

-- 유저 상태 테이블 생성
CREATE TABLE user_statuses
(
    id             UUID,
    create_at      TIMESTAMPTZ,
    update_at      TIMESTAMPTZ,
    user_id        UUID,
    last_active_at TIMESTAMPTZ
);

-- 메시지 읽음 상태 테이블 생성
CREATE TABLE read_statuses
(
    id           UUID,
    create_at    TIMESTAMPTZ,
    update_at    TIMESTAMPTZ,
    user_id      UUID,
    channel_id   UUID,
    last_read_at TIMESTAMPTZ
);
-- << 테이블 생성 끝 >>


-- << 제약조건 수정 시작 >>
-- channels 테이블
CREATE TYPE channels_type AS ENUM ('PUBLIC', 'PRIVATE');
ALTER TABLE channels
    ALTER COLUMN type TYPE channels_type USING type::channels_type;
ALTER TABLE channels
    ALTER COLUMN create_at SET NOT NULL;
ALTER TABLE channels
    ALTER COLUMN type SET NOT NULL;
ALTER TABLE channels
    ADD CONSTRAINT pk_channels PRIMARY KEY (id);

-- binary content 테이블
ALTER TABLE binary_contents
    ALTER COLUMN create_at SET NOT NULL;
ALTER TABLE binary_contents
    ALTER COLUMN file_name SET NOT NULL;
ALTER TABLE binary_contents
    ALTER COLUMN size SET NOT NULL;
ALTER TABLE binary_contents
    ALTER COLUMN content_type SET NOT NULL;
ALTER TABLE binary_contents
    ALTER COLUMN file_path SET NOT NULL;
ALTER TABLE binary_contents
    ADD CONSTRAINT pk_binary_contents PRIMARY KEY (id);

-- users 테이블
ALTER TABLE users
    ALTER COLUMN create_at SET NOT NULL;
ALTER TABLE users
    ALTER COLUMN username SET NOT NULL;
ALTER TABLE users
    ALTER COLUMN email SET NOT NULL;
ALTER TABLE users
    ALTER COLUMN password SET NOT NULL;
ALTER TABLE users
    ALTER COLUMN nickname SET NOT NULL;
ALTER TABLE users
    ALTER COLUMN phone_number SET NOT NULL;
ALTER TABLE users
    ADD CONSTRAINT pk_users PRIMARY KEY (id);
ALTER TABLE users
    ADD CONSTRAINT uk_users_username UNIQUE (username);
ALTER TABLE users
    ADD CONSTRAINT uk_users_email UNIQUE (email);
ALTER TABLE users
    ADD CONSTRAINT fk_users_binary_contents FOREIGN KEY (profile_id) REFERENCES binary_contents (id) ON DELETE SET NULL;

-- message 테이블
ALTER TABLE messages
    ALTER COLUMN create_at SET NOT NULL;
ALTER TABLE messages
    ALTER COLUMN channel_id SET NOT NULL;
ALTER TABLE messages
    ADD CONSTRAINT pk_messages PRIMARY KEY (id);
ALTER TABLE messages
    ADD CONSTRAINT fk_messages_channels FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE;
ALTER TABLE messages
    ADD CONSTRAINT fk_messages_users FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE;

-- message_attachments 테이블
ALTER TABLE message_attachments
    ADD CONSTRAINT fk_messages_attachment_messages FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE;
ALTER TABLE message_attachments
    ADD CONSTRAINT fk_messages_attachment_binary_contents FOREIGN KEY (attachment_id) REFERENCES binary_contents (id) ON DELETE CASCADE;

-- user_statuses 테이블
ALTER TABLE user_statuses
    ALTER COLUMN create_at SET NOT NULL;
ALTER TABLE user_statuses
    ALTER COLUMN user_id SET NOT NULL;
ALTER TABLE user_statuses
    ALTER COLUMN last_active_at SET NOT NULL;
ALTER TABLE user_statuses
    ADD CONSTRAINT pk_user_statuses PRIMARY KEY (id);
ALTER TABLE user_statuses
    ADD CONSTRAINT fk_user_statuses_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE user_statuses
    ADD CONSTRAINT uk_statuses_user_id UNIQUE (user_id);

-- read_statuses 테이블
ALTER TABLE read_statuses
    ALTER COLUMN create_at SET NOT NULL;
ALTER TABLE read_statuses
    ALTER COLUMN last_read_at SET NOT NULL;
ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_statuses_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_statuses_channels FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE;
ALTER TABLE read_statuses
    ADD CONSTRAINT uk_read_statuses_fk UNIQUE (user_id, channel_id);
-- << 제약조건 수정 끝 >>