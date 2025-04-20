CREATE TABLE users
(
    id         uuid PRIMARY KEY,
    created_at timestamptz  NOT NULL,
    updated_at timestamptz,
    username   varchar(50)  NOT NULL,
    email      varchar(100) NOT NULL,
    password   varchar(60)  NOT NULL,
    profile_id uuid,
    UNIQUE (username, email)
);

CREATE TABLE binary_contents
(
    id           uuid PRIMARY KEY,
    created_at   timestamptz  NOT NULL,
    file_name    varchar(255) NOT NULL,
    size         bigint       NOT NULL,
    content_type varchar(100) NOT NULL,
    bytes        bytea        NOT NULL
);


CREATE TABLE channels
(
    id          uuid PRIMARY KEY,
    created_at  timestamptz NOT NULL,
    updated_at  timestamptz,
    name        varchar(100),
    description varchar(500),
    type        varchar(10) NOT NULL
--     CONSTRAINT channels_pkey PRIMARY KEY (id),
--     CONSTRAINT channels_type_check
--         CHECK (type::text = ANY
--                (ARRAY ['PUBLIC'::character varying, 'PRIVATE'::character varying]::text[]))
);

CREATE TABLE messages
(
    id         uuid PRIMARY KEY,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL,
    content    text,
    channel_id uuid        NOT NULL,
    author_id  uuid
--     CONSTRAINT messages_pkey PRIMARY KEY (id),
--     CONSTRAINT fk_author FOREIGN KEY (author_id)
--         REFERENCES public.users (id) MATCH SIMPLE
--         ON UPDATE NO ACTION
--         ON DELETE SET NULL,
--     CONSTRAINT fk_channel FOREIGN KEY (channel_id)
--         REFERENCES public.channels (id) MATCH SIMPLE
--         ON UPDATE NO ACTION
--         ON DELETE CASCADE
);

CREATE TABLE read_statuses
(
    id           uuid PRIMARY KEY,
    created_at   timestamptz NOT NULL,
    updated_at   timestamptz,
    user_id      uuid        NOT NULL,
    last_read_at timestamptz NOT NULL,
    channel_id   uuid        NOT NULL,
    UNIQUE (user_id, channel_id)
--     CONSTRAINT read_statuses_pkey PRIMARY KEY (id),
--     CONSTRAINT read_statuses_channel_id_key UNIQUE (channel_id),
--     CONSTRAINT unique_user_id UNIQUE (user_id),
--     CONSTRAINT fk_channel_id FOREIGN KEY (channel_id)
--         REFERENCES public.channels (id) MATCH SIMPLE
--         ON UPDATE NO ACTION
--         ON DELETE CASCADE,
--     CONSTRAINT fk_user_id FOREIGN KEY (user_id)
--         REFERENCES public.users (id) MATCH SIMPLE
--         ON UPDATE NO ACTION
--         ON DELETE CASCADE
);


CREATE TABLE user_statuses
(
    id             uuid PRIMARY KEY,
    created_at     timestamptz NOT NULL,
    updated_at     timestamptz,
    user_id        uuid UNIQUE NOT NULL,
    last_active_at timestamptz NOT NULL
--     CONSTRAINT user_statuses_pkey PRIMARY KEY (id),
--     CONSTRAINT user_statuses_user_id_key UNIQUE (user_id),
--     CONSTRAINT user_statuses_user_id_fkey FOREIGN KEY (user_id)
--         REFERENCES public.users (id) MATCH SIMPLE
--         ON UPDATE NO ACTION
--         ON DELETE CASCADE
);



CREATE TABLE message_attachments
(
    message_id    uuid,
    attachment_id uuid,
    PRIMARY KEY (message_id, attachment_id)
--     CONSTRAINT fk_attachment FOREIGN KEY (attachment_id)
--         REFERENCES public.binary_contents (id) MATCH SIMPLE
--         ON UPDATE NO ACTION
--         ON DELETE CASCADE,
--     CONSTRAINT fk_message FOREIGN KEY (message_id)
--         REFERENCES public.messages (id) MATCH SIMPLE
--         ON UPDATE NO ACTION
--         ON DELETE CASCADE
);

ALTER TABLE users
    ADD CONSTRAINT fk_user_binary_content
        FOREIGN KEY (profile_id)
            REFERENCES binary_content (id)
            ON DELETE SET NULL