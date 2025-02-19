//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.entity.message.Message;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.repository.file.json.JsonMessageRepository;
//import com.sprint.mission.discodeit.service.MessageService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class BasicMessageServiceWithFileTest {
//
//    MessageRepository messageRepository = new JsonMessageRepository();
//
//    MessageService messageService = new BasicMessageService(messageRepository);
//
//    @Test
//    @DisplayName("메세지 생성")
//    void create() {
//        User sender = new User("sender", "sender@gamil.com", "asddsdasdsad");
//        User receiver = new User("receiver", "receiver@gamil.com", "asddsdasdsadd12d21d");
//
//        Message message = messageService.createMessage("메세지", "하이", sender, receiver);
//
//        assertEquals(message, messageRepository.findMessageById(message.getId()));
//    }
//
//    @Test
//    @DisplayName("단일 메세지 조회")
//    void getId() {
//        User sender = new User("sender", "sender@gamil.com", "asddsdasdsad");
//        User receiver = new User("receiver", "receiver@gamil.com", "asddsdasdsadd12d21d");
//        Message message1 = messageService.createMessage("메세지", "하이", sender, receiver);
//        Message message2 = messageService.createMessage("메세지", "하이", receiver, sender);
//
//
//        Message findMessage = messageService.getMessageById(message1.getId());
//
//        assertEquals(message1, findMessage);
//    }
//
//    @Test
//    @DisplayName("모든 메세지 조회")
//    void getAll() {
//        User sender = new User("sender", "sender@gamil.com", "asddsdasdsad");
//        User receiver = new User("receiver", "receiver@gamil.com", "asddsdasdsadd12d21d");
//        Message message1 = messageService.createMessage("메세지", "하이", sender, receiver);
//        Message message2 = messageService.createMessage("메세지", "하이", receiver, sender);
//        Map<UUID, Message> testMap = new HashMap<>();
//        testMap.put(message1.getId(), message1);
//        testMap.put(message2.getId(), message2);
//
//
//        Map<UUID, Message> allMessages = messageService.getAllMessages();
//
//
//        assertEquals(message1, allMessages.get(message1.getId()));
//        assertEquals(message2, allMessages.get(message2.getId()));
//    }
//
//    @Test
//    @DisplayName("메세지 변경")
//    void update() {
//        User sender = new User("sender", "sender@gamil.com", "asddsdasdsad");
//        User receiver = new User("receiver", "receiver@gamil.com", "asddsdasdsadd12d21d");
//        Message message1 = messageService.createMessage("메세지", "하이", sender, receiver);
//        Message message2 = messageService.createMessage("메세지", "하이", receiver, sender);
//
//
//        messageService.updateMessage(message2.getId(), "newTitle", "newContent");
//
//        assertEquals("newTitle", messageService.getMessageById(message2.getId()).getMessageTitle());
//        assertEquals("newContent", messageService.getMessageById(message2.getId()).getMessageContent());
//
//    }
//
//    @Test
//    @DisplayName("메세지 삭제")
//    void delete() {
//        User sender = new User("sender", "sender@gamil.com", "asddsdasdsad");
//        User receiver = new User("receiver", "receiver@gamil.com", "asddsdasdsadd12d21d");
//        Message message1 = messageService.createMessage("메세지1입니다.", "하이", sender, receiver);
//        Message message2 = messageService.createMessage("메세지2", "하이", receiver, sender);
//
//        messageService.deleteMessage(message1.getId());
//
//        assertThrows(MessageNotFoundException.class,
//                () -> messageService.getMessageById(message1.getId()));
//    }
//}
