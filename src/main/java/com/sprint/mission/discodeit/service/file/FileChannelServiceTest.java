package com.sprint.mission.discodeit.service.file;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.io.InputHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class FileChannelServiceTest {
    private FileChannelService fileChannelService;
    private FileChannelRepository fileChannelRepository;
    private InputHandler inputHandler;
    private UUID testChannelId;
    private final String CHANNELS_PATH = "channels/";
    User user;

    @BeforeEach
    void setUp(){
        user = new User("TestUser");
        fileChannelRepository = new FileChannelRepository();
        inputHandler = mock(InputHandler.class);
        fileChannelService = new FileChannelService(fileChannelRepository, inputHandler);
    }

    @Test
    @DisplayName("채널 생성 테스트")
    void TestCreateChannel(){
        testChannelId = fileChannelService.createChannel(user,"TestChannelCreate");
        assertNotNull(fileChannelService.getChannelById(testChannelId));
    }
    @Test
    @DisplayName("모든 채널 보기 테스트")
    void TestShowAllChannels(){
        testChannelId = fileChannelService.createChannel(user,"TestShowAllChannels");
        assertNotNull(fileChannelService.showAllChannels());
    }
    @Test
    @DisplayName("특정 채널 보기 테스트")
    void TestGetChannelById(){
        testChannelId = fileChannelService.createChannel(user,"TestChannelCreate");
        assertNotNull(fileChannelService.getChannelById(testChannelId));
    }
    @Test
    @DisplayName("모든 채널 삭제 테스트")
    void TestDeleteAllChannels(){
        fileChannelService.createChannel(user,"TestShowAllChannels");
        fileChannelService.deleteAllChannels();
        assertNull(fileChannelService.showAllChannels());
    }
    @Test
    @DisplayName( "특정 채널 삭제 테스트")
    void TestDeleteChannelById(){
        testChannelId = fileChannelService.createChannel(user,"TestChannelCreate");
        fileChannelService.deleteChannelById(testChannelId);
        assertNull(fileChannelService.getChannelById(testChannelId));
    }

    @Test
    @DisplayName("채널 이름 변경 테스트")
    void TestUpdateChannelName(){
        testChannelId = fileChannelService.createChannel(user, "OldChannelName");

        // 새로운 채널 이름 모킹
        String newChannelName = "NewChannelName";
        when(inputHandler.getNewInput()).thenReturn(newChannelName);

        // 채널 이름 변경 실행
        fileChannelService.updateChannelName(testChannelId);
        
        // 불러오기 및 검증
        Channel updatedChannel = fileChannelService.getChannelById(testChannelId);
        assertEquals(newChannelName, updatedChannel.getChannelName());

    }

    @AfterEach
    void tearDown() {
        // 테스트 후 생성된 파일 삭제
        File testFile = new File(CHANNELS_PATH + testChannelId + ".ser");
        if (testFile.exists()) {
            assertTrue(testFile.delete(), "Test user file should be deleted");
        }
    }

}

