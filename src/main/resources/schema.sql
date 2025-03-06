CREATE TYPE "type" AS ENUM (
  'PUBLIC',
  'PRIVATE'
);

CREATE TABLE "users" (
  "id" UUID PRIMARY KEY,
  "created_at" timestamp NOT NULL,
  "updated_at" timestamp,
  "username" varchar(50) UNIQUE NOT NULL,
  "email" varchar(100) UNIQUE NOT NULL,
  "password" varchar(60) NOT NULL,
  "profile_id" uuid
);

CREATE TABLE "binary_contents" (
  "id" UUID PRIMARY KEY,
  "created_at" timestamp NOT NULL,
  "file_name" varchar(255) NOT NULL,
  "size" bigint NOT NULL,
  "content_type" varchar(100) NOT NULL,
  "bytes" bytea NOT NULL
);

CREATE TABLE "user_status" (
  "id" UUID PRIMARY KEY,
  "created_at" timestamp NOT NULL,
  "updated_at" timestamp,
  "user_id" UUID UNIQUE,
  "last_active_at" timestamp NOT NULL
);

CREATE TABLE "channels" (
  "id" UUID PRIMARY KEY,
  "created_at" timestamp NOT NULL,
  "updated_at" timestamp,
  "name" varchar(100),
  "description" varchar(500),
  "type" type NOT NULL
);

CREATE TABLE "read_statuses" (
  "id" UUID PRIMARY KEY,
  "created_at" timestamp NOT NULL,
  "updated_at" timestamp,
  "channel_id" uuid,
  "user_id" UUID NOT NULL,
  "last_read_at" timestamp NOT NULL
);

CREATE TABLE "messages" (
  "id" UUID PRIMARY KEY,
  "created_at" timestamp NOT NULL,
  "updated_at" timestamp,
  "content" text,
  "channel_id" UUID NOT NULL,
  "author_id" UUID
);

CREATE TABLE "message_attachments" (
  "message_id" UUID NOT NULL,
  "attachment_id" UUID
);

COMMENT ON COLUMN "users"."profile_id" IS 'On delete set null';

COMMENT ON COLUMN "user_status"."user_id" IS 'On delete cascade';

COMMENT ON COLUMN "channels"."type" IS 'ENUM(PUBLIC,PRIVATE)';

COMMENT ON COLUMN "read_statuses"."user_id" IS 'on delete cascade';

COMMENT ON COLUMN "messages"."channel_id" IS 'On delete cascade';

COMMENT ON COLUMN "messages"."author_id" IS 'On delete set null';

COMMENT ON COLUMN "message_attachments"."message_id" IS 'On delete cascade';

COMMENT ON COLUMN "message_attachments"."attachment_id" IS 'On delete cascade';

ALTER TABLE "users" ADD FOREIGN KEY ("id") REFERENCES "user_status" ("user_id") ON DELETE CASCADE;

ALTER TABLE "binary_contents" ADD FOREIGN KEY ("id") REFERENCES "users" ("profile_id") ON DELETE SET NULL;

ALTER TABLE "read_statuses" ADD FOREIGN KEY ("channel_id") REFERENCES "channels" ("id") ON DELETE CASCADE;

ALTER TABLE "read_statuses" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE;

ALTER TABLE "messages" ADD FOREIGN KEY ("channel_id") REFERENCES "channels" ("id") ON DELETE CASCADE;

ALTER TABLE "messages" ADD FOREIGN KEY ("author_id") REFERENCES "users" ("id") ON DELETE SET NULL;

ALTER TABLE "message_attachments" ADD FOREIGN KEY ("message_id") REFERENCES "messages" ("id") ON DELETE CASCADE;

ALTER TABLE "binary_contents" ADD FOREIGN KEY ("id") REFERENCES "message_attachments" ("attachment_id") ON DELETE CASCADE;
