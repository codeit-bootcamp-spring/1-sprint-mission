INSERT INTO channels (id, created_at, updated_at, name, description, type)
VALUES ('00000000-0000-0000-0000-000000000001',
        NOW(),
        NOW(),
        'public channel',
        'this is public channel',
        'PUBLIC');

INSERT INTO users (id, created_at, updated_at, username, email, password, profile_id)
VALUES ('00000000-0000-0000-0000-000000000001',
        NOW(),
        NOW(),
        'user1',
        'user1@example.com',
        'encrypted-password',
        null);
INSERT INTO user_statuses (id, created_at, updated_at, user_id, last_active_at)
VALUES ('10000000-0000-0000-0000-000000000001',
        NOW(),
        NOW(),
        '00000000-0000-0000-0000-000000000001',
        NOW());

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('00000000-0000-0000-0000-000000000001',
        NOW(),
        NOW(),
        'content',
        '00000000-0000-0000-0000-000000000001',
        '00000000-0000-0000-0000-000000000001');
