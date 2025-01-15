package com.sprint.mission.discodeit.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MessageTest {
    private static final Logger log = LoggerFactory.getLogger(MessageTest.class);

    private User user= new User("홍길동", "hong@example.com", "hong123", "1234abcd");
    private Channel channel = new Channel("테스트 채널");
    private Message message = new Message(user, channel, "Hello, World!");

    @BeforeEach
    void testStart() {
        log.info("======================== testStart ===========================");
    }

    @AfterEach
    void testEnd() {
        log.info("======================== Test End ============================");
    }

    @Test
    @DisplayName("메시지 업데이트 확인")
    void testUpdateMessageContent() {
        message.update("새로운 메시지 내용");

        assertEquals("새로운 메시지 내용", message.getContent(), "메시지 내용이 업데이트되어야 합니다.");
        assertTrue(message.getUpdatedAt() > message.getCreatedAt(), "수정 시간은 생성 시간보다 뒤이어야 합니다.");
    }

}
