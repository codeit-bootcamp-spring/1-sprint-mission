package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; // mock(), when() 메서드를 사용하기 위한 import

public class JCFChannelServiceTest {
    @Test
    void testCreateChannel(){
        User user = new User("TestUser");

        // 너무... 불필요한 요소가 많이 생기는 것 아닌가
        InputHandler mockInputHandler = mock(InputHandler.class);
        JCFChannelService channelService = new JCFChannelService(mockInputHandler);
        channelService.Create(user, "TestChannelCreate");
        assertNotNull(channelService.getChannel("TestChannelCreate"));
    }

    @Test
    void testReadAllChannels(){
        User user = new User("TestUser");

        InputHandler mockInputHandler = mock(InputHandler.class);
        JCFChannelService channelService = new JCFChannelService(mockInputHandler);
        channelService.Create(user, "TestChannel ReadAll 1");
        channelService.Create(user,"TestChannel ReadAll 2");
        assertEquals(2, channelService.ReadAll());
    }

    @Test
    void testReadChannel(){
        User user = new User("TestUser");

        InputHandler mockInputHandler = mock(InputHandler.class);
        JCFChannelService channelService = new JCFChannelService(mockInputHandler);
        channelService.Create(user,"TestChannel ReadChannel");
        assertNotNull(channelService.ReadChannel("TestChannel ReadChannel"));
    }

    @Test
    void testUpdate(){
        User user = new User("TestUser");

        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getNewInput()).thenReturn("Changed Test channel name");

        JCFChannelService channelService = new JCFChannelService(mockInputHandler);
        channelService.Create(user,"TestChannel Update channelName");
        channelService.Update("TestChannel Update channelName");

        assertEquals("Changed Test channel name", channelService.getChannel("Changed Test channel name").getChannelName());
    }

    @Test
    void testDeleteAll(){
        User user = new User("TestUser");

        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getYesNOInput()).thenReturn("y");


        JCFChannelService channelService = new JCFChannelService(mockInputHandler);
        channelService.Create(user,"testChannel DeleteAll1");
        channelService.Create(user,"testChannel DeleteAll2");
        channelService.DeleteAll();
        assertNull(channelService.getChannel("testChannel DeleteAll1"));
        assertNull(channelService.getChannel("testChannel DeleteAll2"));
    }


    @Test
    void testDeleteChannel(){
        User user = new User("TestUser");

        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getYesNOInput()).thenReturn("y");

        JCFChannelService channelService = new JCFChannelService(mockInputHandler);
        channelService.Create(user,"testUserDeleteUser");
        channelService.DeleteChannel("testUserDeleteUser");

        assertNull(channelService.getChannel("testUserDeleteUser"));
    }
}
