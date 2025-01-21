package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; // mock(), when() 메서드를 사용하기 위한 import

public class JCFChannelServiceTest {
    @Test
    void testCreateChannel(){
        User user = new User("TestUser");
        InputHandler mockInputHandler = mock(InputHandler.class);
        ChannelRepository channelRepository = new JCFChannelRepository();

        JCFChannelService channelService = new JCFChannelService(channelRepository, mockInputHandler);

        UUID id = channelService.createChannel(user, "TestChannelCreate");
        assertNotNull(channelService.getChannelById(id));
    }

    @Test
    void testReadAllChannels(){
        User user = new User("TestUser");

        InputHandler mockInputHandler = mock(InputHandler.class);
        ChannelRepository channelRepository = new JCFChannelRepository();

        JCFChannelService channelService = new JCFChannelService(channelRepository, mockInputHandler);
        channelService.createChannel(user, "TestChannel ReadAll 1");
        channelService.createChannel(user,"TestChannel ReadAll 2");

        assertEquals(2, channelService.showAllChannels());
    }

    @Test
    void testReadChannel(){
        User user = new User("TestUser");

        InputHandler mockInputHandler = mock(InputHandler.class);
        ChannelRepository channelRepository = new JCFChannelRepository();

        JCFChannelService channelService = new JCFChannelService(channelRepository, mockInputHandler);
        UUID id = channelService.createChannel(user,"TestChannel ReadChannel");
        assertNotNull(channelService.getChannelById(id));
    }

    @Test
    void testUpdate(){
        User user = new User("TestUser");

        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getNewInput()).thenReturn("Changed Test channel name");
        ChannelRepository channelRepository = new JCFChannelRepository();

        JCFChannelService channelService = new JCFChannelService(channelRepository, mockInputHandler);
        UUID id = channelService.createChannel(user,"TestChannel Update channelName");
        channelService.updateChannelName(id);
        assertEquals("Changed Test channel name", channelService.getChannelById(id).getChannelName());
    }

    @Test
    void testDeleteAll(){
        User user = new User("TestUser");

        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getYesNOInput()).thenReturn("y");
        ChannelRepository channelRepository = new JCFChannelRepository();


        JCFChannelService channelService = new JCFChannelService(channelRepository, mockInputHandler);
        UUID userId1 = channelService.createChannel(user,"testChannel DeleteAll1");
        UUID userId2 = channelService.createChannel(user,"testChannel DeleteAll2");
        channelService.deleteAllChannels();
        assertNull(channelService.getChannelById(userId1));
        assertNull(channelService.getChannelById(userId2));
    }

    @Test
    void testDeleteChannel(){
        User user = new User("TestUser");

        // Mockito로 InputHandler mock 생성
        InputHandler mockInputHandler = mock(InputHandler.class);
        // mockInputHandler의 메서드가 호출될 때마다 미리 지정된 값 반환
        when(mockInputHandler.getYesNOInput()).thenReturn("y");
        ChannelRepository channelRepository = new JCFChannelRepository();

        JCFChannelService channelService = new JCFChannelService(channelRepository, mockInputHandler);
        UUID id = channelService.createChannel(user,"testUserDeleteUser");
        channelService.deleteChannelById(id);

        assertNull(channelService.getChannelById(id));
    }
}
