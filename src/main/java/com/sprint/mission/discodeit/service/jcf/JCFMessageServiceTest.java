package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; // mock(), when() 메서드를 사용하기 위한 import

public class JCFMessageServiceTest {
    @Test
    void testCreate(){
        User user = new User("TestUser");
        Channel channel = new Channel(user, "TestChannel");

        InputHandler mockInputHandler = mock(InputHandler.class);
        JCFMessageService messageService = new JCFMessageService(mockInputHandler);

        messageService.Create(channel, "Test content");
        assertNotNull(messageService.ReadMessage(0));
    }

    @Test
    void testReadAllMessage(){
        User user = new User("TestUser");
        Channel channel = new Channel(user, "TestChannel");

        InputHandler mockInputHandler = mock(InputHandler.class);
        JCFMessageService messageService = new JCFMessageService(mockInputHandler);

        messageService.Create(channel, "Test content1");
        messageService.Create(channel, "Test content2");
        assertEquals(2, messageService.ReadAll());
    }

    @Test
    void testReadChannel(){
        User user = new User("TestUser");
        Channel channel = new Channel(user, "TestChannel");

        InputHandler mockInputHandler = mock(InputHandler.class);
        JCFMessageService messageService = new JCFMessageService(mockInputHandler);

        messageService.Create(channel, "Test content1");
        assertNotNull(messageService.ReadMessage(0));
    }

    @Test
    void testUpdate(){
        User user = new User("TestUser");
        Channel channel = new Channel(user, "TestChannel");

        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getNewInput()).thenReturn("Changed Test Message content");

        JCFMessageService messageService = new JCFMessageService(mockInputHandler);
        messageService.Create(channel, "Test content1");
        messageService.Update(0);

        assertEquals("Changed Test Message content", messageService.getMessage(0));
    }

    @Test
    void testDeleteAll(){
        User user = new User("TestUser");
        Channel channel = new Channel(user, "TestChannel");

        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getYesNOInput()).thenReturn("y");


        JCFMessageService messageService = new JCFMessageService(mockInputHandler);
        messageService.Create(channel,"testMessage DeleteAll1");
        messageService.Create(channel,"testMessage DeleteAll2");
        messageService.DeleteAll();


        assertTrue(messageService.getMessages().isEmpty());
    }


    @Test
    void testDeleteChannel(){
        User user = new User("TestUser");
        Channel channel = new Channel(user, "TestChannel");

        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getYesNOInput()).thenReturn("y");


        JCFMessageService messageService = new JCFMessageService(mockInputHandler);
        messageService.Create(channel,"testMessage DeleteAll1");
        messageService.DeleteMessage(0);

        assertThrows(IndexOutOfBoundsException.class, () -> messageService.getMessage(0));
    }
}
