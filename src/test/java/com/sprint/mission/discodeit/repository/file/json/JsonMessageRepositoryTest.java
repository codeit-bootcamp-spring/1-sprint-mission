package com.sprint.mission.discodeit.repository.file.json;

import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

class JsonMessageRepositoryTest {

    MessageRepository messageRepository = new JsonMessageRepository();

    @Test
    @DisplayName("Json에 메세지 저장")
    void save() {
        Message m = new Message("title", "contetnt",
                new User("sender", "sender@mail.com", "asdasdaa"),
                new User("receiver", "receiver@mail.com", "clclclclcl"));

        Message saveMessage = messageRepository.saveMessage(m);

        Assertions.assertEquals(m, saveMessage);
    }

    @Test
    @DisplayName("메세지 단일 조회")
    void find() {
        Message m = new Message("title", "contetnt",
                new User("sender", "sender@mail.com", "asdasdaa"),
                new User("receiver", "receiver@mail.com", "clclclclcl"));

        messageRepository.saveMessage(m);

        Message findMessage = messageRepository.findMessageById(m.getId());

        Assertions.assertEquals(m, findMessage);
    }

    @Test
    @DisplayName("모든 메세지 조회")
    void findAll() {
        Message m1 = new Message("title", "contetnt",
                new User("sender", "sender@mail.com", "asdasdaa"),
                new User("receiver", "receiver@mail.com", "clclclclcl"));

        Message m2 = new Message("title", "contetnt",
                new User("sender", "sender@mail.com", "asdasdaa"),
                new User("receiver", "receiver@mail.com", "clclclclcl"));

        messageRepository.saveMessage(m1);
        messageRepository.saveMessage(m2);

        Map<UUID, Message> allMessage = messageRepository.findAllMessage();

        Message find1 = allMessage.get(m1.getId());
        Message find2 = allMessage.get(m2.getId());

        Assertions.assertEquals(m1, find1);
        Assertions.assertEquals(m2, find2);
    }

    @Test
    @DisplayName("메세지 삭제")
    void delete() {
        Message m1 = new Message("title", "contetnt",
                new User("sender", "sender@mail.com", "asdasdaa"),
                new User("receiver", "receiver@mail.com", "clclclclcl"));

        Message m2 = new Message("title", "contetnt",
                new User("sender", "sender@mail.com", "asdasdaa"),
                new User("receiver", "receiver@mail.com", "clclclclcl"));

        messageRepository.saveMessage(m1);
        messageRepository.saveMessage(m2);

        messageRepository.removeMessageById(m1.getId());

        Assertions.assertNull(messageRepository.findMessageById(m1.getId()));

    }

}