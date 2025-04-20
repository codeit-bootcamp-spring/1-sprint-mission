CREATE TYPE type AS ENUM (
    'PUBLIC',
    'PRIVATE'
    );

CREATE TABLE binary_contents
(
    "id"           UUID PRIMARY KEY,
    "created_at"   timestamptz  NOT NULL,
    "file_name"    varchar(255) NOT NULL,
    "size"         bigint       NOT NULL,
    "content_type" varchar(100) NOT NULL
);

CREATE TABLE users
(
    "id"         UUID PRIMARY KEY,
    "created_at" timestamptz         NOT NULL,
    "updated_at" timestamptz,
    "username"   varchar(50) UNIQUE  NOT NULL,
    "email"      varchar(100) UNIQUE NOT NULL,
    "password"   varchar(60)         NOT NULL,
    "profile_id" UUID,
    FOREIGN KEY ("profile_id") REFERENCES "binary_contents" ("id") ON DELETE SET NULL
);

CREATE TABLE user_statuses
(
    "id"             UUID PRIMARY KEY,
    "created_at"     timestamptz NOT NULL,
    "updated_at"     timestamptz,
    "user_id"        UUID UNIQUE,
    "last_active_at" timestamptz NOT NULL,
    FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE
);

CREATE TABLE "channels"
(
    "id"          UUID PRIMARY KEY,
    "created_at"  timestamptz NOT NULL,
    "updated_at"  timestamptz,
    "name"        varchar(100),
    "description" varchar(500),
    "type"        type        NOT NULL
);

CREATE TABLE "read_statuses"
(
    "id"           UUID PRIMARY KEY,
    "created_at"   timestamptz NOT NULL,
    "updated_at"   timestamptz,
    "channel_id"   UUID,
    "user_id"      UUID,
    "last_read_at" timestamptz NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE,
    CONSTRAINT unique_user_channel UNIQUE (channel_id, user_id)
);

CREATE TABLE messages
(
    "id"         UUID PRIMARY KEY,
    "created_at" timestamptz NOT NULL,
    "updated_at" timestamptz,
    "content"    text,
    "channel_id" UUID        NOT NULL,
    "author_id"  UUID,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE SET NULL,
    FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE
);

CREATE TABLE "message_attachments"
(
    "id"            UUID PRIMARY KEY,
    "created_at"    timestamptz NOT NULL,
    "message_id"    UUID        NOT NULL,
    "attachment_id" UUID        NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE,
    FOREIGN KEY (attachment_id) REFERENCES binary_contents (id) ON DELETE CASCADE,
    CONSTRAINT unique_message_attachment UNIQUE (message_id, attachment_id)
);
