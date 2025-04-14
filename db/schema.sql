-- 테이블이 이미 존재하면 드롭
DROP TABLE IF EXISTS read_statuses CASCADE;
DROP TABLE IF EXISTS message_attachments CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS user_statuses CASCADE;
DROP TABLE IF EXISTS channels CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS binary_contents CASCADE;

-- binary_contents 테이블 생성
CREATE TABLE binary_contents (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP,
    content_type VARCHAR(255),
    original_filename VARCHAR(255),
    file_path VARCHAR(255),
    file_size BIGINT
);

-- users 테이블 생성
CREATE TABLE users (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    online BOOLEAN DEFAULT FALSE,
    profile_id UUID,
    FOREIGN KEY (profile_id) REFERENCES binary_contents(id)
);

-- channels 테이블 생성
CREATE TABLE channels (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    type VARCHAR(50) NOT NULL
);

-- messages 테이블 생성
CREATE TABLE messages (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    content TEXT NOT NULL,
    author_id UUID,
    channel_id UUID NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (channel_id) REFERENCES channels(id)
);

-- message_attachments 테이블 생성
CREATE TABLE message_attachments (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP,
    message_id UUID NOT NULL,
    attachment_id UUID NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE,
    FOREIGN KEY (attachment_id) REFERENCES binary_contents(id)
);

-- user_statuses 테이블 생성
CREATE TABLE user_statuses (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    user_id UUID NOT NULL,
    status_message TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- read_statuses 테이블 생성
CREATE TABLE read_statuses (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    user_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    last_read_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (channel_id) REFERENCES channels(id),
    UNIQUE (user_id, channel_id)
); 