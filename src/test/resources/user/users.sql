INSERT INTO users (id, username, email, password, created_at, updated_at)
VALUES ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'user1', 'user1@example.com', 'password123', NOW(), NOW()),
       ('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'user2', 'user2@example.com', 'password456', NOW(), NOW()),
       ('ccccccc3-cccc-cccc-cccc-cccccccccccc', 'user3', 'user3@example.com', 'password789', NOW(), NOW()),
       ('ddddddd4-dddd-dddd-dddd-dddddddddddd', 'user4', 'user4@example.com', 'password000', NOW(), NOW());

INSERT INTO user_statuses (id, user_id, last_active_at, created_at, updated_at)
VALUES ('11111111-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '2025-03-31T00:00:00Z', NOW(),
        NOW()),
       ('22222222-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '2025-03-31T00:00:00Z', NOW(),
        NOW()),
       ('33333333-cccc-cccc-cccc-cccccccccccc', 'ccccccc3-cccc-cccc-cccc-cccccccccccc', '2025-03-31T00:00:00Z', NOW(),
        NOW()),
       ('44444444-dddd-dddd-dddd-dddddddddddd', 'ddddddd4-dddd-dddd-dddd-dddddddddddd', '2025-03-31T00:00:00Z', NOW(),
        NOW());
