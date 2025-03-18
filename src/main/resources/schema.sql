CREATE TABLE users
(
    id         UUID    NOT NULL,
    created_at TIMESTAMP           NOT NULL,
    updated_at TIMESTAMP           NOT NULL,
    username   VARCHAR(50) NOT NULL,
    email      VARCHAR(100) NOT NULL,
    password   VARCHAR(60)         NOT NULL,
    profile_id UUID
);

-- PRIMARY KEY 추가
ALTER TABLE users ADD CONSTRAINT pk_users PRIMARY KEY (id);

-- UNIQUE 제약조건 추가
ALTER TABLE users ADD CONSTRAINT uk_users_username UNIQUE (username);
ALTER TABLE users ADD CONSTRAINT uk_users_email UNIQUE (email);

-- FOREIGN KEY 추가
ALTER TABLE users ADD CONSTRAINT fk_users_profile FOREIGN KEY (profile_id)
    REFERENCES binary_contents (id) ON DELETE CASCADE;


CREATE TABLE binary_contents
(
    id           UUID,
    created_at   TIMESTAMP    NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    bytes        BYTEA        NOT NULL
);

-- PRIMARY KEY 추가
ALTER TABLE binary_contents ADD CONSTRAINT pk_binary_contents PRIMARY KEY (id);


CREATE TABLE message_attachments
(
    message_id UUID,
    attachment_id UUID
);

-- FOREIGN KEY 추가
ALTER TABLE message_attachments ADD CONSTRAINT fk_message_attachments_message
    FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE;

ALTER TABLE message_attachments ADD CONSTRAINT fk_message_attachments_attachment
    FOREIGN KEY (attachment_id) REFERENCES binary_contents (id) ON DELETE CASCADE;


CREATE TABLE messages
(
    id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    content TEXT,
    channel_id UUID NOT NULL,
    author_id UUID
);

-- PRIMARY KEY 추가
ALTER TABLE messages ADD CONSTRAINT pk_message PRIMARY KEY (id);
-- FOREIGN KEY 추가 (이 친구들 하기 전에 channels, users 테이블 생성 + 기본키 생성해놔야 한다.)
ALTER TABLE messages ADD CONSTRAINT fk_messages_channel
    FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE;

ALTER TABLE messages ADD CONSTRAINT fk_messages_author
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE SET NULL;

CREATE TYPE channel_type AS ENUM ('PUBLIC', 'PRIVATE');

CREATE TABLE channels
(
    id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    name VARCHAR(100),
    description VARCHAR(500),
    type channel_type NOT NULL
);

-- PRIMARY KEY 추가
ALTER TABLE channels ADD CONSTRAINT pk_channels PRIMARY KEY (id);

CREATE TABLE read_statuses
(
    id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    user_id UUID,
    channel_id UUID,
    last_read_at TIMESTAMP NOT NULL
);
-- PRIMARY KEY 추가
ALTER TABLE read_statuses ADD CONSTRAINT pk_read_statuses PRIMARY KEY (id);
-- UNIQUE 제약조건 추가
ALTER TABLE read_statuses ADD CONSTRAINT uk_read_statuses_user_channel
    UNIQUE (user_id, channel_id);
-- FOREIGN KEY 추가
ALTER TABLE read_statuses ADD CONSTRAINT fk_read_statuses_user
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE read_statuses ADD CONSTRAINT fk_read_statuses_channel
    FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE;


CREATE TABLE user_statuses(
    id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    user_id UUID NOT NULL,
    last_active_at TIMESTAMP NOT NULL
);

-- PRIMARY KEY 추가
ALTER TABLE user_statuses ADD CONSTRAINT pk_user_statuses PRIMARY KEY (id);
-- UNIQUE 제약조건 추가
ALTER TABLE user_statuses ADD CONSTRAINT uk_user_statuses_user UNIQUE (user_id);
-- FOREIGN KEY 추가
ALTER TABLE user_statuses ADD CONSTRAINT fk_user_statuses_user
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;


ALTER TABLE binary_contents DROP COLUMN bytes -- 이후 binaryContents 고도화 전략의 조건으로 bytes 컬럼 제거