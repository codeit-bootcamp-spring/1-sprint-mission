CREATE TABLE users (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    profile_image_id UUID
);

CREATE TABLE binary_contents (
 id UUID PRIMARY KEY,
 created_at TIMESTAMPTZ NOT NULL ,
 type_id UUID,
 file_name VARCHAR(255),
 size BIGINT NOT NULL,
 content_type VARCHAR(100)
    --,  bytes BYTEA NOT NULL
);

CREATE TABLE user_statuses (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL ,
    updated_at TIMESTAMPTZ NOT NULL ,
    user_id UUID UNIQUE NOT NULL ,
    last_seen_at TIMESTAMPTZ
);

CREATE TABLE channels (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL ,
    updated_at TIMESTAMPTZ,
    name VARCHAR(100),
    description VARCHAR(500),
    type VARCHAR(10) CHECK ( type IN ('PUBLIC', 'PRIVATE')) NOT NULL
);

CREATE TABLE messages (
    id UUID PRIMARY KEY ,
    created_at TIMESTAMPTZ NOT NULL ,
    updated_at TIMESTAMPTZ ,
    content TEXT,
    sender_id UUID NOT NULL ,
    recipient_id UUID,
    channel_id UUID NOT NULL
);

DROP TABLE binary_contents CASCADE;

SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name = 'binary_contents';

ALTER TABLE users ADD COLUMN phone_number VARCHAR(20);

CREATE TABLE read_statuses (
    id UUID PRIMARY KEY ,
    created_at TIMESTAMPTZ NOT NULL ,
    updated_at TIMESTAMPTZ ,
    user_id UUID NOT NULL ,
    channel_id UUID NOT NULL ,
    last_read_at TIMESTAMPTZ NOT NULL ,
    UNIQUE (user_id, channel_id)
);

ALTER TABLE users
    ADD CONSTRAINT fk_user_profile_image
        FOREIGN KEY (profile_image_id)
            REFERENCES binary_contents(id)
            ON DELETE CASCADE;

SELECT * FROM binary_contents;

SELECT schemaname, tablename FROM pg_tables WHERE tablename = 'binary_contents';

SELECT current_database();