package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class BasicMessageServiceWithJCFTest {

    MessageRepository messageRepository = new JCFMessageRepository();

    MessageService messageService = new BasicMessageService(messageRepository);

    @Test
    @DisplayName("메세지 생성")
    void create() {
        User sender = new User("sender", "sender@gamil.com", "asddsdasdsad");
        User receiver = new User("receiver", "receiver@gamil.com", "asddsdasdsadd12d21d");

        Message message = messageService.createMessage("메세지", "하이", sender, receiver);

        Assertions.assertEquals(message, messageRepository.findMessageById(message.getMessageId()));
    }

    @Test
    @DisplayName("단일 메세지 조회")
    void getMessageID() {
        User sender = new User("sender", "sender@gamil.com", "asddsdasdsad");
        User receiver = new User("receiver", "receiver@gamil.com", "asddsdasdsadd12d21d");
        Message message1 = messageService.createMessage("메세지", "하이", sender, receiver);
        Message message2 = messageService.createMessage("메세지", "하이", receiver, sender);


        Message findMessage = messageService.getMessageById(message1.getMessageId());

        Assertions.assertEquals(message1, findMessage);
    }

    @Test
    @DisplayName("모든 메세지 조회")
    void getAll() {
        User sender = new User("sender", "sender@gamil.com", "asddsdasdsad");
        User receiver = new User("receiver", "receiver@gamil.com", "asddsdasdsadd12d21d");
        Message message1 = messageService.createMessage("메세지", "하이", sender, receiver);
        Message message2 = messageService.createMessage("메세지", "하이", receiver, sender);
        Map<UUID, Message> testMap = new HashMap<>();
        testMap.put(message1.getMessageId(), message1);
        testMap.put(message2.getMessageId(), message2);


        Map<UUID, Message> allMessages = messageService.getAllMessages();


        Assertions.assertEquals(testMap, allMessages);
    }

    @Test
    @DisplayName("메세지 변경")
    void update() {
        User sender = new User("sender", "sender@gamil.com", "asddsdasdsad");
        User receiver = new User("receiver", "receiver@gamil.com", "asddsdasdsadd12d21d");
        Message message1 = messageService.createMessage("메세지", "하이", sender, receiver);
        Message message2 = messageService.createMessage("메세지", "하이", receiver, sender);


        messageService.updateMessage(message2.getMessageId(), "newTitle", "newContent");

        Assertions.assertEquals("newTitle", messageService.getMessageById(message2.getMessageId()).getMessageTitle());
        Assertions.assertEquals("newContent", messageService.getMessageById(message2.getMessageId()).getMessageContent());

    }

    @Test
    @DisplayName("메세지 삭제")
    void delete() {
        User sender = new User("sender", "sender@gamil.com", "asddsdasdsad");
        User receiver = new User("receiver", "receiver@gamil.com", "asddsdasdsadd12d21d");
        Message message1 = messageService.createMessage("메세지11", "하이11", sender, receiver);
        Message message2 = messageService.createMessage("메세지", "하이", receiver, sender);

        messageService.deleteMessage(message1.getMessageId());

        Assertions.assertThrows(MessageNotFoundException.class, () -> messageService.getMessageById(message1.getMessageId()));

    }
}