-- DROP TABLE IF EXISTS public.binary_contents;
CREATE TABLE IF NOT EXISTS public.binary_contents
(
    id uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    file_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    size bigint NOT NULL,
    content_type character varying(100) COLLATE pg_catalog."default" NOT NULL,
    bytes bytea NOT NULL,
    CONSTRAINT binary_contents_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.users
(
    id uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    email character varying(50) COLLATE pg_catalog."default" NOT NULL,
    password character varying(60) COLLATE pg_catalog."default" NOT NULL,
    profile_id uuid,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_email_key UNIQUE (email),
    CONSTRAINT users_username_key UNIQUE (username),
    CONSTRAINT users_profile_id_fkey FOREIGN KEY (profile_id)
        REFERENCES public.binary_contents (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS public.channels(
    id uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    name character varying(100) COLLATE pg_catalog."default",
    description character varying(500) COLLATE pg_catalog."default",
    type character varying(10) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT channels_pkey PRIMARY KEY (id),
    CONSTRAINT channels_type_check
        CHECK (type::text = ANY (ARRAY['PUBLIC'::character varying, 'PRIVATE'::character varying]::text[]))
);

CREATE TABLE IF NOT EXISTS public.messages
(
    id uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    content text COLLATE pg_catalog."default",
    channel_id uuid NOT NULL,
    author_id uuid,
    CONSTRAINT messages_pkey PRIMARY KEY (id),
    CONSTRAINT fk_author FOREIGN KEY (author_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE SET NULL,
    CONSTRAINT fk_channel FOREIGN KEY (channel_id)
        REFERENCES public.channels (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.read_statuses
(
    id uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    user_id uuid,
    last_read_at timestamp with time zone NOT NULL,
    channel_id uuid,
    CONSTRAINT read_statuses_pkey PRIMARY KEY (id),
    CONSTRAINT read_statuses_channel_id_key UNIQUE (channel_id),
    CONSTRAINT unique_user_id UNIQUE (user_id),
    CONSTRAINT fk_channel_id FOREIGN KEY (channel_id)
        REFERENCES public.channels (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS public.user_statuses
(
    id uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    user_id uuid NOT NULL,
    last_active_at timestamp with time zone NOT NULL,
    CONSTRAINT user_statuses_pkey PRIMARY KEY (id),
    CONSTRAINT user_statuses_user_id_key UNIQUE (user_id),
    CONSTRAINT user_statuses_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);



CREATE TABLE IF NOT EXISTS public.message_attachments
(
    message_id uuid,
    attachment_id uuid,
    CONSTRAINT fk_attachment FOREIGN KEY (attachment_id)
        REFERENCES public.binary_contents (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_message FOREIGN KEY (message_id)
        REFERENCES public.messages (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);