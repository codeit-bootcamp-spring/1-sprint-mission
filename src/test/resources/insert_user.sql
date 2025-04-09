INSERT INTO users (id, created_at, updated_at, username, email, password, profile_id)
VALUES ('00000000-0000-0000-0000-000000000001',
        NOW(),
        NOW(),
        'user1',
        'user1@example.com',
        'encrypted-password',
        null);

INSERT INTO users (id, created_at, updated_at, username, email, password, profile_id)
VALUES ('00000000-0000-0000-0000-000000000002',
        NOW(),
        NOW(),
        'user2',
        'user2@example.com',
        'encrypted-password',
        null);

INSERT INTO user_statuses (id, created_at, updated_at, user_id, last_active_at)
VALUES ('10000000-0000-0000-0000-000000000001',
        NOW(),
        NOW(),
        '00000000-0000-0000-0000-000000000001',
        NOW());

INSERT INTO user_statuses (id, created_at, updated_at, user_id, last_active_at)
VALUES ('20000000-0000-0000-0000-000000000001',
        NOW(),
        NOW(),
        '00000000-0000-0000-0000-000000000002',
        NOW());
