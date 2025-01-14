package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.impl.InMemoryMessageRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; // mock(), when() 메서드를 사용하기 위한 import

public class JCFMessageServiceTest {
    @Test
    void testCreate(){
        User user = new User("TestUser");
        Channel channel = new Channel(user, "TestChannel");

        InputHandler mockInputHandler = mock(InputHandler.class);
        MessageRepository messageRepository = new InMemoryMessageRepository();

        JCFMessageService messageService = new JCFMessageService(messageRepository, mockInputHandler);

        UUID id = messageService.createMessage(channel, "Test Text");
        assertNotNull(messageService.getMessageById(id));
    }

    @Test
    void testReadAllMessage(){
        User user = new User("TestUser");
        Channel channel = new Channel(user, "TestChannel");

        InputHandler mockInputHandler = mock(InputHandler.class);
        MessageRepository messageRepository = new InMemoryMessageRepository();

        JCFMessageService messageService = new JCFMessageService(messageRepository, mockInputHandler);

        messageService.createMessage(channel, "Test content1");
        messageService.createMessage(channel, "Test content2");
        assertEquals(2, messageService.showAllMessages());
    }

    @Test
    void testReadChannel(){
        User user = new User("TestUser");
        Channel channel = new Channel(user, "TestChannel");

        InputHandler mockInputHandler = mock(InputHandler.class);
        MessageRepository messageRepository = new InMemoryMessageRepository();

        JCFMessageService messageService = new JCFMessageService(messageRepository, mockInputHandler);

        UUID id = messageService.createMessage(channel, "Test content1");
        assertNotNull(messageService.getMessageById(id));
    }

    @Test
    void testUpdate(){
        User user = new User("TestUser");
        Channel channel = new Channel(user, "TestChannel");

        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        MessageRepository messageRepository = new InMemoryMessageRepository();

        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getNewInput()).thenReturn("Changed Test Message content");

        JCFMessageService messageService = new JCFMessageService(messageRepository, mockInputHandler);
        UUID id = messageService.createMessage(channel, "Test content1");
        messageService.updateMessageText(id);

        assertEquals("Changed Test Message content", messageService.getMessageById(id).getMessageText());
    }

    @Test
    void testDeleteAll(){
        User user = new User("TestUser");
        Channel channel = new Channel(user, "TestChannel");

        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getYesNOInput()).thenReturn("y");
        MessageRepository messageRepository = new InMemoryMessageRepository();

        JCFMessageService messageService = new JCFMessageService(messageRepository, mockInputHandler);

        UUID MessageId1 = messageService.createMessage(channel,"testMessage DeleteAll1");
        UUID MessageId2 = messageService.createMessage(channel,"testMessage DeleteAll2");
        messageService.deleteAllMessages();

        assertEquals(0, messageService.showAllMessages());
    }


    @Test
    void testDeleteMessage(){
        User user = new User("TestUser");
        Channel channel = new Channel(user, "TestChannel");

        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getYesNOInput()).thenReturn("y");
        MessageRepository messageRepository = new InMemoryMessageRepository();

        JCFMessageService messageService = new JCFMessageService(messageRepository, mockInputHandler);

        UUID id = messageService.createMessage(channel,"testMessage Delete");
        messageService.deleteMessageById(id);

        assertNull(messageService.getMessageById(id));
    }
}
