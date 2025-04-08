-- Users
INSERT INTO users (id, created_at, updated_at, username, email, password, profile_id)
VALUES ('00000000-0000-0000-0000-000000000001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'userA',
        'userA@email.com', 'pass', NULL),
       ('00000000-0000-0000-0000-000000000002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'userB',
        'userB@email.com', 'pass', NULL);

-- User Statuses (각 사용자당 하나)
INSERT INTO user_statuses (id, created_at, updated_at, user_id, last_active_at)
VALUES ('00000000-0000-0000-0000-000000000101', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '00000000-0000-0000-0000-000000000001', CURRENT_TIMESTAMP),
       ('00000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '00000000-0000-0000-0000-000000000002', CURRENT_TIMESTAMP);

-- 공개 채널 1개 (수정/삭제용)
INSERT INTO channels (id, name, description, type, created_at, updated_at)
VALUES ('00000000-0000-0000-0000-00000000abcd', 'old name', 'old desc', 'PUBLIC', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

-- 공개 채널 1개 (삭제용)
INSERT INTO channels (id, name, description, type, created_at, updated_at)
VALUES ('00000000-0000-0000-0000-00000000bbcc', '공개채널2', '설명2', 'PUBLIC', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

-- 공개 채널 (사용자 조회 대상)
INSERT INTO channels (id, name, description, type, created_at, updated_at)
VALUES ('00000000-0000-0000-0000-00000000cafe', '공개채널', '설명', 'PUBLIC', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

-- 비공개 채널
INSERT INTO channels (id, name, description, type, created_at, updated_at)
VALUES ('00000000-0000-0000-0000-00000000beef', null, null, 'PRIVATE', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

-- userA가 비공개 채널 참여
INSERT INTO read_statuses (id, created_at, updated_at, user_id, channel_id, last_read_at)
VALUES ('00000000-0000-0000-0000-00000000fafa',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '00000000-0000-0000-0000-000000000001',
        '00000000-0000-0000-0000-00000000beef',
        CURRENT_TIMESTAMP);

-- 공개 채널 (메시지 테스트용)
INSERT INTO channels (id, name, description, type, created_at, updated_at)
VALUES ('00000000-0000-0000-0000-00000000aaaa', '테스트채널', '메시지 테스트용', 'PUBLIC', CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

-- 메시지 (수정 대상)
INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('00000000-0000-0000-0000-00000000dddd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '원본 메시지입니다', '00000000-0000-0000-0000-00000000cafe',
        '00000000-0000-0000-0000-000000000001');

-- 메시지 (삭제 대상)
INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('00000000-0000-0000-0000-00000000eeee', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '삭제할 메시지입니다', '00000000-0000-0000-0000-00000000cafe',
        '00000000-0000-0000-0000-000000000001');

-- 메시지 (목록 조회용) - 채널: 00000000-0000-0000-0000-00000000aaaa, 작성자: userA
INSERT INTO messages (id, created_at, updated_at, content, channel_id, author_id)
VALUES ('00000000-0000-0000-0000-00000000aa01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '안녕하세요!', '00000000-0000-0000-0000-00000000aaaa', '00000000-0000-0000-0000-000000000001'),

       ('00000000-0000-0000-0000-00000000aa02', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,
        '테스트 메시지입니다', '00000000-0000-0000-0000-00000000aaaa',
        '00000000-0000-0000-0000-000000000001');