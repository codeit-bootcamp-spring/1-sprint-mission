SET search_path TO public;

CREATE TABLE IF NOT EXISTS persistent_logins (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) PRIMARY KEY,
    token VARCHAR(64) NOT NULL,
    last_used timestamp with time zone NOT NULL
);

-- binary_contents 테이블 (파일 저장)
CREATE TABLE IF NOT EXISTS binary_contents (
    id UUID PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    bytes BYTEA
);

-- users 테이블
CREATE TABLE IF NOT EXISTS "users" (
    id UUID PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(60) NOT NULL,
    role VARCHAR(30) NOT NULL DEFAULT 'ROLE_USER',
    profile_id UUID,
    CONSTRAINT fk_users_profile FOREIGN KEY (profile_id) REFERENCES binary_contents(id) ON DELETE set null
);

-- channels 테이블 (채널 정보)
CREATE TABLE IF NOT EXISTS channels (
    id UUID PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    name VARCHAR(100),
    description VARCHAR(500),
    type VARCHAR(10) NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

-- messages 테이블 (메시지 정보)
CREATE TABLE IF NOT EXISTS messages (
    id UUID PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    content TEXT,
    channel_id UUID NOT NULL,
    author_id UUID,
    CONSTRAINT fk_messages_channel FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
    CONSTRAINT fk_messages_author FOREIGN KEY (author_id) REFERENCES "users"(id) ON DELETE SET NULL
);

-- read_statuses 테이블 (메시지 읽음 상태)
CREATE TABLE IF NOT EXISTS read_statuses (
    id UUID PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    user_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    last_read_at timestamp with time zone,
    CONSTRAINT fk_read_statuses_user FOREIGN KEY (user_id) REFERENCES "users"(id) ON DELETE CASCADE,
    CONSTRAINT fk_read_statuses_channel FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
    UNIQUE (user_id, channel_id)
);

-- message_attachments 테이블 (메시지 첨부 파일)
CREATE TABLE IF NOT EXISTS message_attachments (
    message_id UUID NOT NULL,
    attachment_id UUID NOT NULL,
    CONSTRAINT fk_message_attachments_message FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE,
    CONSTRAINT fk_message_attachments_attachment FOREIGN KEY (attachment_id) REFERENCES binary_contents(id) ON DELETE CASCADE
);
