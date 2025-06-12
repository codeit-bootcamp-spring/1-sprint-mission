/*CREATE TABLE IF NOT EXISTS binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP WITH TIME ZONE  NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    content_type VARCHAR(100) NOT NULL
    );
*/

CREATE TABLE IF NOT EXISTS binary_contents
(
    id             UUID PRIMARY KEY,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    file_name      VARCHAR(255)             NOT NULL,
    size           BIGINT                   NOT NULL,
    content_type   VARCHAR(100)             NOT NULL,
    upload_status  VARCHAR(10)              NOT NULL DEFAULT 'WAITING'
    );

CREATE TABLE IF NOT EXISTS users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE         NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(60)         NOT NULL,
    profile_id UUID                REFERENCES binary_contents (id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at     TIMESTAMP WITH TIME ZONE,
    user_id        UUID        NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    last_active_at TIMESTAMP WITH TIME ZONE NOT NULL
                                                                         );

CREATE TABLE IF NOT EXISTS channels
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE                                       NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10) CHECK (type IN ('PUBLIC', 'PRIVATE')) NOT NULL
    );

CREATE TABLE IF NOT EXISTS messages
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    content    TEXT,
    channel_id UUID        NOT NULL REFERENCES channels (id) ON DELETE CASCADE,
    author_id  UUID        REFERENCES users (id) ON DELETE SET NULL
    );

/*CREATE TABLE IF NOT EXISTS read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE,
    user_id      UUID        NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    channel_id   UUID        NOT NULL REFERENCES channels (id) ON DELETE CASCADE,
    last_read_at TIMESTAMP WITH TIME ZONE,
                                                                UNIQUE (user_id, channel_id)
    );*/

CREATE TABLE IF NOT EXISTS read_statuses
(
    id                     UUID PRIMARY KEY,
    created_at             TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at             TIMESTAMP WITH TIME ZONE,
    user_id                UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    channel_id             UUID NOT NULL REFERENCES channels (id) ON DELETE CASCADE,
    last_read_at           TIMESTAMP WITH TIME ZONE,
    notification_enabled   BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (user_id, channel_id)
    );

CREATE TABLE IF NOT EXISTS message_attachments
(
    message_id    UUID NOT NULL REFERENCES messages (id) ON DELETE CASCADE,
    attachment_id UUID NOT NULL REFERENCES binary_contents (id) ON DELETE CASCADE,
    PRIMARY KEY (message_id, attachment_id)
    );