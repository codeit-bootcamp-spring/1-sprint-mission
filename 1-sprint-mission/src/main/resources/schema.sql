CREATE TABLE users(
                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                      created_at TIMESTAMPT NOT NULL DEFAULT now(),
                      updated_at TIMESTAMPT DEFAULT now(),
                      username VARCHAR(50) UNIQUE NOT NULL,
                      email VARCHAR(100) UNIQUE NOT NULL,
                      password VARCHAR(60) NOT NULL,
                      profile_id UUID NULL,
                      CONSTRAINT fk_users_profile FOREIGN KEY (profile_id) REFERENCES binary_contents(id) ON DELETE SET NULL
);

CREATE TABLE channels(
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         created_at TIMESTAMPT NOT NULL DEFAULT now(),
                         updated_at TIMESTAMPT NOT NULL DEFAULT now(),
                         name VARCHAR(100),
                         description VARCHAR(500),
                         type VARCHAR(10) NOT NULL CHECK(type IN ('PUBLIC', 'PRIVATE'))
);

CREATE TABLE messages(
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         created_at TIMESTAMPT NOT NULL DEFAULT now(),
                         updated_at TIMESTAMPT NOT NULL DEFAULT now(),
                         content TEXT,
                         channel_id UUID NOT NULL ,
                         author_id UUID NULL,
                         CONSTRAINT fk_messages_channel FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
                         CONSTRAINT fk_messages_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE read_statuses(
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              created_at TIMESTAMPT NOT NULL DEFAULT now(),
                              updated_at TIMESTAMPT NOT NULL DEFAULT now(),
                              user_id UUID NOT NULL,
                              channel_id UUID NOT NULL,
                              last_read_at TIMESTAMPTZ NOT NULL,
                              CONSTRAINT fk_read_status_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                              CONSTRAINT fk_read_status_channel FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
                              UNIQUE (user_id, channel_id)
);

CREATE TABLE user_statuses(
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              created_at TIMESTAMPT NOT NULL DEFAULT now(),
                              updated_at TIMESTAMPT NOT NULL DEFAULT now(),
                              user_id UUID NOT NULL UNIQUE,
                              last_active_at TIMESTAMPT NOT NULL,
                              CONSTRAINT fk_user_status FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE binary_contents(
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                created_at TIMESTAMPT NOT NULL DEFAULT now(),
                                file_name VARCHAR(255) NOT NULL,
                                size BIGINT NOT NULL,
                                content_type VARCHAR(100) NOT NULL,
                                bytes BYTEA NOT NULL
);

CREATE TABLE message_attachments(
                                    message_id UUID NOT NULL,
                                    attachment_id UUID NOT NULL,
                                    CONSTRAINT fk_attachment_message FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE,
                                    CONSTRAINT fk_attachment_binary FOREIGN KEY (attachment_id) REFERENCES binary_contents(id) ON DELETE CASCADE,
                                    PRIMARY KEY (message_id, attachment_id)
);

