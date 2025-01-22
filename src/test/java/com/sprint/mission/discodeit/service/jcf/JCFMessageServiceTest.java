package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JCFMessageServiceTest {
    private JCFMessageService messageService;
    private User sender;

    @BeforeEach
    void setUp() {
        messageService = new JCFMessageService();
        sender = new User("1111", "Alice");
    }

    @DisplayName("일반 메시지 생성 테스트")
    @Test
    void testCreateCommonMessage() {
        // given
        String content = "Hello, World!";

        // when
        messageService.createCommonMessage(sender, content);

        // then
        assertEquals(1, messageService.findAllMessages().size());
        assertEquals(content, messageService.findAllMessages().get(0).getContent());
    }

    @DisplayName("답장 메시지 생성 테스트")
    @Test
    void testCreateReplyMessage() {
        // given
        String content = "Hello, World!";

        // when
        messageService.createReplyMessage(sender, content);

        // then
        assertEquals(1, messageService.findAllMessages().size());
        assertEquals(content, messageService.findAllMessages().get(0).getContent());
    }

    @DisplayName("메시지 단건 조회 테스트")
    @Test
    void testFindMessageById() {
        // given
        messageService.createCommonMessage(sender, "Hello, World!");

        // when
        String content = messageService.findAllMessages().get(0).getContent();

        // then
        assertEquals(content, messageService.findMessageById(messageService.findAllMessages().get(0).getId()).getContent());
    }

    @DisplayName("메시지 전체 조회 테스트")
    @Test
    void testFindAllMessages() {
        // given
        messageService.createCommonMessage(sender, "Hello, World!");
        messageService.createCommonMessage(sender, "Hello, World!");

        // when
        int size = messageService.findAllMessages().size();

        // then
        assertEquals(size, messageService.findAllMessages().size());
    }

    @DisplayName("메시지 업데이트 테스트")
    @Test
    void testUpdateMessage() {
        // given
        messageService.createCommonMessage(sender, "Hello, World!");

        // when
        String newContent = "Hello, World!2";
        messageService.updateMessage(messageService.findAllMessages().get(0).getId(), newContent);

        // then
        assertEquals(newContent, messageService.findAllMessages().get(0).getContent());
    }

    @DisplayName("메시지 삭제 테스트")
    @Test
    void testRemoveMessage() {
        // given
        messageService.createCommonMessage(sender, "Hello, World!");

        // when
        messageService.removeMessage(messageService.findAllMessages().get(0).getId());

        // then
        assertEquals(0, messageService.findAllMessages().size());
    }
}