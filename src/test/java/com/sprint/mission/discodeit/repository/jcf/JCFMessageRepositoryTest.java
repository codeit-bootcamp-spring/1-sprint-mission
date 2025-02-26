//package com.sprint.mission.discodeit.repository.jcf;
//
//import com.sprint.mission.discodeit.entity.message.Message;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//class JCFMessageRepositoryTest {
//
//    MessageRepository messageRepository = new JCFMessageRepository();
//
//    @BeforeEach
//    void init() {
//        messageRepository = new JCFMessageRepository();
//    }
//
//    @Test
//    @DisplayName("메시지 저장")
//    void save() {
//        Message m = new Message("title", "contetnt",
//                new User("sender", "sender@mail.com", "asdasdaa"),
//                new User("receiver", "receiver@mail.com", "clclclclcl"));
//        messageRepository.saveMessage(m);
//
//        Message message = messageRepository.saveMessage(m);
//
//        Assertions.assertEquals(m, message);
//    }
//
//    @Test
//    @DisplayName("메세지 단일 조회")
//    void find() {
//        Message m = new Message("title", "contetnt",
//                new User("sender", "sender@mail.com", "asdasdaa"),
//                new User("receiver", "receiver@mail.com", "clclclclcl"));
//        messageRepository.saveMessage(m);
//
//        Message message = messageRepository.findMessageById(m.getId());
//
//        Assertions.assertEquals(m, message);
//    }
//
//    @Test
//    @DisplayName("모든 메세지 조회")
//    void findALL() {
//        Message m1 = new Message("title", "contetnt",
//                new User("sender", "sender@mail.com", "asdasdaa"),
//                new User("receiver", "receiver@mail.com", "clclclclcl"));
//
//        Message m2 = new Message("title", "contetnt",
//                new User("sender", "sender@mail.com", "asdasdaa"),
//                new User("receiver", "receiver@mail.com", "clclclclcl"));
//        Map<UUID, Message> testMap = new HashMap<>();
//        testMap.put(m1.getId(), m1);
//        testMap.put(m2.getId(), m2);
//
//        messageRepository.saveMessage(m1);
//        messageRepository.saveMessage(m2);
//
//        Map<UUID, Message> allMessage = messageRepository.findAllMessage();
//
//        Assertions.assertEquals(testMap, allMessage);
//
//    }
//
//    @Test
//    @DisplayName("메세지 삭제")
//    void delete() {
//        Message m1 = new Message("title", "contetnt",
//                new User("sender", "sender@mail.com", "asdasdaa"),
//                new User("receiver", "receiver@mail.com", "clclclclcl"));
//
//        Message m2 = new Message("title", "contetnt",
//                new User("sender", "sender@mail.com", "asdasdaa"),
//                new User("receiver", "receiver@mail.com", "clclclclcl"));
//        messageRepository.saveMessage(m1);
//        messageRepository.saveMessage(m2);
//
//        messageRepository.removeMessageById(m1.getId());
//
//        Assertions.assertNull(messageRepository.findMessageById(m1.getId()));
//
//    }
//}