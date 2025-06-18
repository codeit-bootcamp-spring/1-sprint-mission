create table binary_contents(
	id uuid primary key,
	created_at timestamp not null,
	file_name varchar(255) not null,
	size bigint not null,
	content_type varchar(100) not null,
    upload_status varchar(20) not null default 'WAITING'
);

create table users(
	id uuid primary key,
	created_at timestamp not null,
	updated_at timestamp,
	username varchar(50) not null unique,
	email varchar(100) not null unique,
	password varchar(255) not null,
    role varchar(30) not null default 'ROLE_USER',
	profile_id uuid references binary_contents(id) on delete set null
);

create type channel_type as enum('PRIVATE', 'PUBLIC');

create table channels(
	id uuid primary key,
	created_at timestamp not null,
	updated_at timestamp,
	name varchar(100),
	description varchar(500),
	type channel_type not null
);

create table read_statuses(
	id uuid primary key,
	created_at timestamp not null,
	updated_at timestamp,
	user_id uuid references users(id) on delete cascade,
	channel_id uuid references channels(id) on delete cascade,
	last_read_at timestamp not null,
	constraint read_statuses_user_id_channel_id_key UNIQUE (user_id, channel_id)
);

create table messages(
	id uuid primary key,
	created_at timestamp not null,
	updated_at timestamp,
	content text,
	channel_id uuid not null references channels(id) on delete cascade,
	author_id uuid references users(id) on delete set null
);

create table message_attachments(
	message_id uuid references messages(id) on delete cascade,
	attachment_id uuid references binary_contents(id) on delete cascade,
    primary key (message_id, attachment_id)
);

create table jwt_sessions(
    id uuid primary key,
    created_at timestamp not null,
    updated_at timestamp,
    user_id uuid not null,
    access_token varchar(2048) not null unique,
    refresh_token varchar(2048) not null unique,
    expiration_time timestamp not null
);

create table notifications(
    id uuid primary key,
    created_at timestamp not null,
    receiver_id uuid not null,
    title varchar(255) not null,
    content varchar(512) not null,
    type varchar(20) not null,
    target_id uuid
);