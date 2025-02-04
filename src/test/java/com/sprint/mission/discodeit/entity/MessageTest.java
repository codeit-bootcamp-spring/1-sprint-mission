package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.message.NullMessageContentException;
import com.sprint.mission.discodeit.exception.message.NullMessageTitleException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @DisplayName("메세지 내용이 비어있을 때 검증")
    void checkMessageNoneContentException() {
        assertThrows(NullMessageContentException.class, () -> messageService.createMessage("title", "", sender, receiver));
    }

    @Test
    @DisplayName("메세지 제목이 비어있을 때 검증")
    void checkMessageNoneTitleException() {
        assertThrows(NullMessageTitleException.class, () -> messageService.createMessage("", "content", sender, receiver));
    }

    @Test
    @DisplayName("보내는 사람과 받는 사람이 없을 때")
    void checkAllOfNullSenderAndReceiver() {
        assertThrows(UserNotFoundException.class, () -> new Message("hi", "hello?", null, null));
    }

    @Test
    @DisplayName("보내는 사람이 없을 때")
    void checkNullSender() {
        assertThrows(UserNotFoundException.class, () -> new Message("hi", "hello?", null, receiver));
    }

    @Test
    @DisplayName("받는 사람이 없을 때")
    void checkNullReceiver() {
        assertThrows(UserNotFoundException.class, () -> new Message("hi", "hello?", sender, null));
    }

    @Test
    @DisplayName("보내는 사람이 똑같을 때")
    void checkSenderToSender() {
        assertThrows(UserNotFoundException.class, () -> new Message("hi", "hello?", sender, sender));
    }
}