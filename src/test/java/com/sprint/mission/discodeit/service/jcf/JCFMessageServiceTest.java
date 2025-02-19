//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.entity.message.Message;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.exception.message.NullMessageContentException;
//import com.sprint.mission.discodeit.service.MessageService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.Map;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class JCFMessageServiceTest {
//    MessageService messageService = new JCFMessageService();
//    User sender;
//    User receiver;
//    String title = "Test Title";
//    String content = "Test Content";
//
//    String senderUserName = "testUser";
//    String receiverUserName = "testUser";
//
//    @BeforeEach
//    void init() {
//        sender = new User(senderUserName, "sender@mail.com" , "password1111");
//        receiver = new User(receiverUserName, "reciver@mail.com" , "password1111");
//    }
//
//    @Test
//    @DisplayName("메시지 생성 테스트")
//    void createMessageTest() {
//        Message message = messageService.createMessage(title, content, sender, receiver);
//
//        assertNotNull(message);
//        assertEquals(title, message.getMessageTitle());
//        assertEquals(content, message.getMessageContent());
//        assertEquals(sender, message.getMessageSendUser());
//        assertEquals(receiver, message.getMessageReceiveUser());
//    }
//
//    @Test
//    @DisplayName("메시지 생성 시 제목이 null인 경우 예외 발생")
//    void createMessageWithNullTitleTest() {
//        assertThrows(NullMessageContentException.class, () ->
//                messageService.createMessage(null, content, sender, receiver)
//        );
//    }
//
//    @Test
//    @DisplayName("메시지 ID로 조회 테스트")
//    void getMessageByIdTest() {
//        Message createdMessage = messageService.createMessage(title, content, sender, receiver);
//
//        Message foundMessage = messageService.getMessageById(createdMessage.getId());
//
//        assertNotNull(foundMessage);
//        assertEquals(createdMessage.getId(), foundMessage.getId());
//        assertEquals(title, foundMessage.getMessageTitle());
//    }
//
//    @Test
//    @DisplayName("전체 메시지 조회 테스트")
//    void getAllMessagesTest() {
//        messageService.createMessage("Title 1", "Content 1", sender, receiver);
//        messageService.createMessage("Title 2", "Content 2", sender, receiver);
//
//        Map<UUID, Message> messages = messageService.getAllMessages();
//
//        assertNotNull(messages);
//        assertTrue(messages.size() >= 2);
//    }
//
//    @Test
//    @DisplayName("메시지 업데이트 테스트")
//    void updateMessageTest() {
//        Message message = messageService.createMessage(title, content, sender, receiver);
//        String newTitle = "Updated Title";
//        String newContent = "Updated Content";
//
//        Message updatedMessage = messageService.updateMessage(message.getId(), newTitle, newContent);
//
//        assertEquals(newTitle, updatedMessage.getMessageTitle());
//        assertEquals(newContent, updatedMessage.getMessageContent());
//    }
//
//    @Test
//    @DisplayName("메시지 업데이트 시 제목이 null인 경우 예외 발생")
//    void updateMessageWithNullTitleTest() {
//        Message message = messageService.createMessage(title, content, sender, receiver);
//
//        assertThrows(NullMessageContentException.class, () ->
//                messageService.updateMessage(message.getId(), null, "New Content")
//        );
//    }
//
//    @Test
//    @DisplayName("메시지 삭제 테스트")
//    void deleteMessageTest() {
//        Message message = messageService.createMessage(title, content, sender, receiver);
//
//        messageService.deleteMessage(message.getId());
//
//        Map<UUID, Message> messages = messageService.getAllMessages();
//    }
//}