-- 유저 초기화
INSERT INTO users (id, created_at, username, email, password)
VALUES ('3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0', CURRENT_TIMESTAMP, 'user1', 'user1@mail.com',
        'password123');

INSERT INTO users (id, created_at, username, email, password)
VALUES ('4b3d2e1f-7c0a-5f9b-b8d6-e5c3f2d1e0b9', CURRENT_TIMESTAMP, 'user2', 'user2@mail.com',
        'password123');

INSERT INTO users (id, created_at, username, email, password)
VALUES ('5c4e3f2d-8d1b-6a0c-c9e7-f6d4e2c0b9a8', CURRENT_TIMESTAMP, 'user3', 'user3@mail.com',
        'password123');


-- 채널 초기화
INSERT INTO channels (id, created_at, updated_at, name, description, type)
VALUES ('6f17a8d1-77d7-437d-811e-d98db3bd30bc', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        'ch1',
        'channel 1', 'PUBLIC');

INSERT INTO channels (id, created_at, updated_at, name, description, type)
VALUES ('1f17a8d1-77d7-437d-811e-d98db3bd30bc', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        'ch2',
        'channel 2', 'PUBLIC');

INSERT INTO channels (id, created_at, updated_at, name, description, type)
VALUES ('0017a8d1-77d7-437d-811e-d98db3bd30bc', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        null,
        null, 'PRIVATE');

INSERT INTO channels (id, created_at, updated_at, name, description, type)
VALUES ('3417a8d1-77d7-437d-811e-d98db3bd30bc', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        null,
        null, 'PRIVATE');


-- 유저의 read status 초기화
INSERT INTO read_statuses (id, created_at, updated_at, last_read_at, user_id, channel_id)
VALUES ('5e8cf92c-f1de-4a66-b364-6e9379df1476', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0', '6f17a8d1-77d7-437d-811e-d98db3bd30bc');

INSERT INTO read_statuses (id, created_at, updated_at, last_read_at, user_id, channel_id)
VALUES ('4e7cf92c-f1de-4a66-b364-6e9379df1476', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        '4b3d2e1f-7c0a-5f9b-b8d6-e5c3f2d1e0b9', '6f17a8d1-77d7-437d-811e-d98db3bd30bc');

INSERT INTO read_statuses (id, created_at, updated_at, last_read_at, user_id, channel_id)
VALUES ('2e7cf92c-f1de-4a66-b364-6e9379df1476', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP,
        '4b3d2e1f-7c0a-5f9b-b8d6-e5c3f2d1e0b9', '1f17a8d1-77d7-437d-811e-d98db3bd30bc');

-- 메세지 초기화
INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('39358af2-7bf8-4b1a-ac79-32f417ce391c',
        '2025-03-31 02:43:54.899593 +00:00',
        '2025-03-31 02:43:54.899593 +00:00',
        '안녕하세요',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('4c0ace2e-a5b4-45dd-8d5e-8ab9f03e4d12',
        '2025-03-31 02:43:54.899593 +00:00',
        '2025-03-31 02:43:54.899593 +00:00',
        '테스트1',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('b6cbbe31-9d24-4c2b-aff4-192ca63cd3d5',
        '2025-03-31 02:43:54.899593 +00:00',
        '2025-03-31 02:43:54.899593 +00:00',
        '테스트1',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('a30ed81d-d033-45b1-86b1-9047a73c0d5c',
        '2025-03-31 02:43:54.336997 +00:00',
        '2025-03-31 02:43:54.336997 +00:00',
        '테스트1',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('00bd5e0d-fa31-441d-b9d4-f2e37dd3df2b',
        '2025-03-31 02:43:54.207895 +00:00',
        '2025-03-31 02:43:54.207895 +00:00',
        '테스트2',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('a69457cf-2930-4cfe-8be2-2727c12f62ff',
        '2025-03-31 02:43:54.051977 +00:00',
        '2025-03-31 02:43:54.051977 +00:00',
        '테스트3',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('2bdcc519-9854-483b-bce5-1f52c705b0a0',
        '2025-03-31 02:43:53.931035 +00:00',
        '2025-03-31 02:43:53.931035 +00:00',
        '테스트4',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('3ae7d16a-d74e-4770-b265-1bbc7b3495f4',
        '2025-03-31 02:43:53.780114 +00:00',
        '2025-03-31 02:43:53.780114 +00:00',
        '테스트5',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('0d45af7d-d395-4a72-8ad3-899faf18ebf3',
        '2025-03-31 02:43:53.661669 +00:00',
        '2025-03-31 02:43:53.661669 +00:00',
        '테스트6',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('c153db2b-a8ec-447d-9ddb-3a36f01299e2',
        '2025-03-31 02:43:53.528598 +00:00',
        '2025-03-31 02:43:53.528598 +00:00',
        '테스트7',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('45d5ff86-0cf4-4d04-92c7-8fbf4ccc3cc8',
        '2025-03-31 02:43:53.290292 +00:00',
        '2025-03-31 02:43:53.290292 +00:00',
        '테스트8',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('294fd309-b25a-4d70-80a2-eae8c714b699',
        '2025-03-31 02:43:53.151426 +00:00',
        '2025-03-31 02:43:53.151426 +00:00',
        '테스트9',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('0c4eec05-48d6-4adf-9111-939e8c076fb0',
        '2025-03-31 02:43:53.002352 +00:00',
        '2025-03-31 02:43:53.002352 +00:00',
        '테스트10',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('3af3a5ff-d0ba-4972-a3e4-24717dd6f7bb',
        '2025-03-31 02:43:52.887014 +00:00',
        '2025-03-31 02:43:52.887014 +00:00',
        '테스트11',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('3ffb7182-d589-4037-80d9-307d0aea99d9',
        '2025-03-31 02:43:52.754626 +00:00',
        '2025-03-31 02:43:52.754626 +00:00',
        '테스트12',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('04714d71-2c90-4f1f-9da8-d4e12874138e',
        '2025-03-31 02:43:52.633865 +00:00',
        '2025-03-31 02:43:52.633865 +00:00',
        '테스트13',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('816a4b74-d6fe-47b2-a617-785ccbd05e5f',
        '2025-03-31 02:43:52.376284 +00:00',
        '2025-03-31 02:43:52.376284 +00:00',
        '테스트14',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('cfd66e29-0cdf-4363-a44b-cd1692424dff',
        '2025-03-31 02:43:52.247437 +00:00',
        '2025-03-31 02:43:52.247437 +00:00',
        '테스트15',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('1e9a1d76-c2c1-4ca4-8c1f-b1216b69ef07',
        '2025-03-31 02:43:52.107272 +00:00',
        '2025-03-31 02:43:52.107272 +00:00',
        '테스트16',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('2e9a1d76-c2c1-4ca4-8c1f-b1216b69ef07',
        '2025-03-30 02:43:52.107272 +00:00',
        '2025-03-30 02:43:52.107272 +00:00',
        '테스트16',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');


INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('3e9a1d76-c2c1-4ca4-8c1f-b1216b69ef07',
        '2025-03-29 02:43:52.107272 +00:00',
        '2025-03-29 02:43:52.107272 +00:00',
        '테스트16',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');

INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('4e9a1d76-c2c1-4ca4-8c1f-b1216b69ef07',
        '2025-03-28 02:43:52.107272 +00:00',
        '2025-03-28 02:43:52.107272 +00:00',
        '테스트16',
        '6f17a8d1-77d7-437d-811e-d98db3bd30bc',
        '3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0');