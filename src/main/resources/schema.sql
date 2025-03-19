CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMPTZ         NOT NULL,
    updated_at TIMESTAMPTZ         NOT NULL,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(60)         NOT NULL,
    profile_id UUID,
    FOREIGN KEY (profile_id) REFERENCES binary_contents (id) ON DELETE SET NULL
);

CREATE TABLE channels
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMPTZ  NOT NULL,
    updated_at  TIMESTAMPTZ  NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    type        VARCHAR(10)  NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    content    TEXT,
    channel_id UUID        NOT NULL,
    author_id  UUID,
    FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE SET NULL
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
    updated_at     TIMESTAMPTZ NOT NULL,
    user_id        UUID        NOT NULL,
    last_active_at TIMESTAMPTZ NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    UNIQUE (user_id)
);

CREATE TABLE read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMPTZ NOT NULL,
    updated_at   TIMESTAMPTZ NOT NULL,
    user_id      UUID        NOT NULL,
    channel_id   UUID        NOT NULL,
    last_read_at TIMESTAMPTZ NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE,
    UNIQUE (user_id, channel_id)
);

CREATE TABLE message_attachments
(
    message_id    UUID NOT NULL,
    attachment_id UUID NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE,
    FOREIGN KEY (attachment_id) REFERENCES binary_contents (id) ON DELETE CASCADE
);

-- -- discodeit 데이터베이스용 DDL
-- -- 제공된 ERD를 기반으로 작성됨
--
-- -- 테이블이 존재할 경우 삭제 (외래 키 제약조건을 피하기 위해 역순으로 삭제)
-- DROP TABLE IF EXISTS message_attachments CASCADE;
-- DROP TABLE IF EXISTS messages CASCADE;
-- DROP TABLE IF EXISTS read_statuses CASCADE;
-- DROP TABLE IF EXISTS user_statuses CASCADE;
-- DROP TABLE IF EXISTS binary_contents CASCADE;
-- DROP TABLE IF EXISTS channels CASCADE;
-- DROP TABLE IF EXISTS users CASCADE;
--
-- -- 사용자 테이블 생성
-- CREATE TABLE users (
--                        uuid UUID PRIMARY KEY,
--                        id VARCHAR(255) NOT NULL UNIQUE,
--                        created_at TIMESTAMP NOT NULL,
--                        updated_at TIMESTAMP,
--                        username VARCHAR(50) NOT NULL UNIQUE,
--                        email VARCHAR(100) NOT NULL UNIQUE,
--                        password VARCHAR(60) NOT NULL,
--                        profile_id UUID,
--                        CONSTRAINT fk_profile_id FOREIGN KEY (profile_id) REFERENCES binary_contents(uuid) ON DELETE SET NULL
-- );
--
-- -- 바이너리 콘텐츠 테이블 생성
-- CREATE TABLE binary_contents (
--                                  uuid UUID PRIMARY KEY,
--                                  id VARCHAR(255) NOT NULL UNIQUE,
--                                  created_at TIMESTAMP NOT NULL,
--                                  file_name VARCHAR(255) NOT NULL,
--                                  size BIGINT NOT NULL,
--                                  content_type VARCHAR(100) NOT NULL,
--                                  bytes BYTEA NOT NULL
-- );
--
-- -- 채널 테이블 생성
-- CREATE TABLE channels (
--                           uuid UUID PRIMARY KEY,
--                           id VARCHAR(255) NOT NULL UNIQUE,
--                           created_at TIMESTAMP NOT NULL,
--                           updated_at TIMESTAMP,
--                           name VARCHAR(100) NOT NULL,
--                           description VARCHAR(500),
--                           type VARCHAR(10) NOT NULL,
--                           CONSTRAINT check_channel_type CHECK (type IN ('PUBLIC', 'PRIVATE'))
-- );
--
-- -- 사용자 상태 테이블 생성
-- CREATE TABLE user_statuses (
--                                uuid UUID PRIMARY KEY,
--                                id VARCHAR(255) NOT NULL UNIQUE,
--                                created_at TIMESTAMP NOT NULL,
--                                updated_at TIMESTAMP,
--                                user_id UUID NOT NULL,
--                                last_active_at TIMESTAMP NOT NULL,
--                                CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(uuid) ON DELETE CASCADE
-- );
--
-- -- 읽음 상태 테이블 생성
-- CREATE TABLE read_statuses (
--                                uuid UUID PRIMARY KEY,
--                                id VARCHAR(255) NOT NULL UNIQUE,
--                                created_at TIMESTAMP NOT NULL,
--                                updated_at TIMESTAMP,
--                                user_id UUID NOT NULL,
--                                channel_id UUID NOT NULL,
--                                last_read_at TIMESTAMP NOT NULL,
--                                CONSTRAINT fk_read_status_user_id FOREIGN KEY (user_id) REFERENCES users(uuid) ON DELETE CASCADE,
--                                CONSTRAINT fk_channel_id FOREIGN KEY (channel_id) REFERENCES channels(uuid) ON DELETE CASCADE
-- );
--
-- -- 메시지 테이블 생성
-- CREATE TABLE messages (
--                           uuid UUID PRIMARY KEY,
--                           id VARCHAR(255) NOT NULL UNIQUE,
--                           created_at TIMESTAMP NOT NULL,
--                           updated_at TIMESTAMP,
--                           content TEXT,
--                           channel_id UUID NOT NULL,
--                           author_id UUID,
--                           CONSTRAINT fk_message_channel_id FOREIGN KEY (channel_id) REFERENCES channels(uuid) ON DELETE CASCADE,
--                           CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES users(uuid) ON DELETE SET NULL
-- );
--
-- -- 메시지 첨부파일 테이블 생성
-- CREATE TABLE message_attachments (
--                                      uuid UUID PRIMARY KEY,
--                                      message_id UUID NOT NULL,
--                                      attachment_id UUID NOT NULL,
--                                      CONSTRAINT fk_message_id FOREIGN KEY (message_id) REFERENCES messages(uuid) ON DELETE CASCADE,
--                                      CONSTRAINT fk_attachment_id FOREIGN KEY (attachment_id) REFERENCES binary_contents(uuid) ON DELETE CASCADE
-- );
--
-- -- 모든 테이블이 생성된 후 순환 참조 추가
-- ALTER TABLE users ADD CONSTRAINT fk_profile_id FOREIGN KEY (profile_id) REFERENCES binary_contents(uuid) ON DELETE SET NULL;
--
-- -- 성능 향상을 위한 인덱스 생성
-- CREATE INDEX idx_user_username ON users(username);
-- CREATE INDEX idx_user_email ON users(email);
-- CREATE INDEX idx_channel_name ON channels(name);
-- CREATE INDEX idx_message_channel_id ON messages(channel_id);
-- CREATE INDEX idx_message_author_id ON messages(author_id);
-- CREATE INDEX idx_read_status_user_channel ON read_statuses(user_id, channel_id);
-- CREATE INDEX idx_user_status_user_id ON user_statuses(user_id);
-- CREATE INDEX idx_message_attachments_message_id ON message_attachments(message_id);
-- CREATE INDEX idx_message_attachments_attachment_id ON message_attachments(attachment_id);