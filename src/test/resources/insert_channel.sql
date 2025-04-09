INSERT INTO channels (id, created_at, updated_at, name, description, type)
VALUES ('00000000-0000-0000-0000-000000000001',
        NOW(),
        NOW(),
        'public channel',
        'this is public channel',
        'PUBLIC');

INSERT INTO channels (id, created_at, updated_at, name, description, type)
VALUES ('00000000-0000-0000-0000-000000000002',
        NOW(),
        NOW(),
        null,
        null,
        'PRIVATE');
