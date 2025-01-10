package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    MessageService messageService = new JCFMessageService();
    User sender = new User("sender", "sender@gmail.com" ,"123456");
    User receiver = new User("receiver", "sendme@naver.com", "dsadssdasasd");

    @BeforeEach
    void init() {
        messageService = new JCFMessageService();
    }

    @Test
    @DisplayName("메세지가 제대로 생성되었는지 확인")
    void checkMessageContentNone() {
        Message message = messageService.createMessage("title", "content", sender, receiver);

        Assertions.assertEquals("title", message.getMessageTitle());
        Assertions.assertEquals("content", message.getMessageContent());
        Assertions.assertEquals(sender.getUserId(), message.getMessageSendUser().getUserId());
        Assertions.assertEquals(receiver.getUserId(), message.getMessageReceiveUser().getUserId());
    }

    @Test
    @DisplayName("메세지 제목이나 내용이 비어있을 때 검증")
    void checkMessageContentNoneException() {
        assertThrows(IllegalArgumentException.class, () -> messageService.createMessage("", "content", sender, receiver));

        assertThrows(IllegalArgumentException.class, () -> messageService.createMessage("title", "", sender, receiver));
    }

    @Test
    @DisplayName("메세지를 보내거나 받는 사람 검증")
    void checkSenderAndReceiver() {
        assertThrows(IllegalArgumentException.class, () -> new Message("hi", "hello?", null, null));
        assertThrows(IllegalArgumentException.class, () -> new Message("hi", "hello?", null, receiver));
        assertThrows(IllegalArgumentException.class, () -> new Message("hi", "hello?", sender, null));
        assertThrows(IllegalArgumentException.class, () -> new Message("hi", "hello?", sender, sender));
    }
}