-- public.binary_contents definition

-- Drop table

-- DROP TABLE public.binary_contents;

CREATE TABLE public.binary_contents (
	id uuid DEFAULT uuid_generate_v4() NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	file_name varchar(255) NOT NULL,
	"size" int8 NOT NULL,
	content_type varchar(100) NOT NULL,
	bytes bytea NOT NULL,
	CONSTRAINT binary_contents_pkey PRIMARY KEY (id)
);


-- public.channels definition

-- Drop table

-- DROP TABLE public.channels;

CREATE TABLE public.channels (
	id uuid DEFAULT uuid_generate_v4() NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_at timestamp NULL,
	"name" varchar(100) NULL,
	description varchar(500) NULL,
	"type" varchar(10) NOT NULL,
	CONSTRAINT channels_pkey PRIMARY KEY (id)
);


-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
	id uuid DEFAULT uuid_generate_v4() NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_at timestamp NULL,
	username varchar(50) NOT NULL,
	email varchar(100) NOT NULL,
	"password" varchar(60) NOT NULL,
	profile_id uuid NULL,
	CONSTRAINT users_email_key UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT users_username_key UNIQUE (username),
	CONSTRAINT fk_profile_id FOREIGN KEY (profile_id) REFERENCES public.binary_contents(id) ON DELETE CASCADE
);


-- public.messages definition

-- Drop table

-- DROP TABLE public.messages;

CREATE TABLE public.messages (
	id uuid DEFAULT uuid_generate_v4() NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_at timestamp NULL,
	"content" text NULL,
	channel_id uuid NOT NULL,
	author_id uuid NULL,
	CONSTRAINT messages_pkey PRIMARY KEY (id),
	CONSTRAINT fk_author_id FOREIGN KEY (author_id) REFERENCES public.users(id) ON DELETE SET NULL,
	CONSTRAINT fk_channel_id FOREIGN KEY (channel_id) REFERENCES public.channels(id) ON DELETE CASCADE
);


-- public.read_statuses definition

-- Drop table

-- DROP TABLE public.read_statuses;

CREATE TABLE public.read_statuses (
	id uuid DEFAULT uuid_generate_v4() NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_at timestamp NULL,
	user_id uuid NULL,
	channel_id uuid NULL,
	last_read_at timestamp NOT NULL,
	CONSTRAINT read_statuses_pkey PRIMARY KEY (id),
	CONSTRAINT unique_user_channel UNIQUE (user_id, channel_id),
	CONSTRAINT fk_channel_id FOREIGN KEY (channel_id) REFERENCES public.channels(id) ON DELETE CASCADE,
	CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE
);


-- public.user_statuses definition

-- Drop table

-- DROP TABLE public.user_statuses;

CREATE TABLE public.user_statuses (
	id uuid DEFAULT uuid_generate_v4() NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_at timestamp NULL,
	user_id uuid NOT NULL,
	last_active_at timestamp NOT NULL,
	CONSTRAINT user_statuses_pkey PRIMARY KEY (id),
	CONSTRAINT user_statuses_user_id_key UNIQUE (user_id),
	CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE
);


-- public.message_attachments definition

-- Drop table

-- DROP TABLE public.message_attachments;

CREATE TABLE public.message_attachments (
	message_id uuid NULL,
	attachment_id uuid NULL,
	CONSTRAINT fk_attachment_id FOREIGN KEY (attachment_id) REFERENCES public.binary_contents(id) ON DELETE CASCADE,
	CONSTRAINT fk_message_id FOREIGN KEY (message_id) REFERENCES public.messages(id) ON DELETE CASCADE
);