package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileMassageServiceTest {
    private FileMassageService fileMessageService;
    private FileMessageRepository fileMessageRepository;
    private InputHandler inputHandler;
    private UUID testMessageId;
    private final String MESSAGES_PATH = "messages/";
    User user;
    Channel channel;

    @BeforeEach
    void setUp(){
        user = new User("TestUser");
        channel = new Channel(user, "TestChannel");
        fileMessageRepository = new FileMessageRepository();
        inputHandler = mock(InputHandler.class);
        fileMessageService = new FileMassageService(fileMessageRepository, inputHandler);
    }
    @Test
    @DisplayName("메세지 생성 테스트")
    void TestCreateMessage(){
        testMessageId = fileMessageService.createMessage(channel, "TestMessageText");
        assertNotNull(fileMessageService.getMessageById(testMessageId));
    }
    @Test
    @DisplayName("모든 메세지 보기 테스트")
    void TestShowAllMessages(){
        fileMessageService.createMessage(channel, "TestMessageText");
        assertEquals(1, fileMessageService.showAllMessages());
    }
    @Test
    @DisplayName("특정 메세지 보기 테스트")
    void TestGetMessageById(){
        testMessageId = fileMessageService.createMessage(channel, "TestMessageText");
        assertNotNull(fileMessageService.getMessageById(testMessageId));
    }
    @Test
    @DisplayName("모든 메세지 삭제 테스트")
    void TestDeleteAllMessages(){
        fileMessageService.createMessage(channel, "TestMessageText");
        fileMessageService.deleteAllMessages();
        assertEquals(0, fileMessageService.showAllMessages());
    }
    @Test
    @DisplayName( "특정 메세지 삭제 테스트")
    void TestDeleteMessageById(){
        testMessageId = fileMessageService.createMessage(channel, "TestMessageText");
        fileMessageService.deleteMessageById(testMessageId);
        assertNull(fileMessageService.getMessageById(testMessageId));
    }

    @Test
    @DisplayName("메세지 내용 변경 테스트")
    void TestUpdateChannelName(){
        testMessageId = fileMessageService.createMessage(channel, "TestMessageText");

        // 새로운 내용 모킹
        String newMessageText = "NewMessageText";
        when(inputHandler.getNewInput()).thenReturn(newMessageText);

        // 채널 이름 변경 실행
        fileMessageService.updateMessageText(testMessageId);

        // 불러오기 및 검증
        Message updatedMessage = fileMessageService.getMessageById(testMessageId);
        assertEquals(newMessageText, updatedMessage.getMessageText());

    }

    @AfterEach
    void tearDown() {
        // 테스트 후 생성된 파일 삭제
        File testFile = new File(MESSAGES_PATH + testMessageId + ".ser");
        if (testFile.exists()) {
            assertTrue(testFile.delete(), "Test user file should be deleted");
        }
    }
}
