package com.sprint.mission.discodeit.service.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ChannelService;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

class FileMessageServiceTest {
    private static final String TEST_FILE_PATH = "data/test_messages.dat";
    private FileMessageService messageService;

    @Mock
    private ChannelService mockChannelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // TODO anyString()일 경우 create와 get이 성공하게 해둠
        when(mockChannelService.createChannel(anyString())).thenReturn(new Channel("Return Channel"));
        when(mockChannelService.getChannel(any())).thenReturn(Optional.of(new Channel("Return Channel")));

        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            assertTrue(file.delete(), "테스트 파일 삭제 실패");
        }
        messageService = new FileMessageService(TEST_FILE_PATH, mockChannelService);
    }

    @Test
    @DisplayName("메시지를 생성한다")
    void testCreateMessage() {
        // given
        UUID authorID = UUID.randomUUID();
        UUID channelID = mockChannelService.createChannel("Test Channel").getId();
        String text = "Hello, world!";

        // when
        Optional<Message> message = messageService.createMessage(authorID, channelID, text);

        // then
        assertNotNull(message);
        assertEquals(text, message.orElseThrow().getText());
        assertEquals(authorID, message.orElseThrow().getAuthorId());
        assertEquals(channelID, message.orElseThrow().getChannelId());
        assertTrue(messageService.getMessages().containsKey(message.orElseThrow().getId()));
    }

    @Test
    @DisplayName("메시지를 생성하고 단일 조회를 한다")
    void testGetMessage() {
        // given
        UUID authorID = UUID.randomUUID();
        UUID channelID = mockChannelService.createChannel("Test Channel").getId();
        Optional<Message> message = messageService.createMessage(authorID, channelID, "Hello, world!");

        // when
        Optional<Message> foundMessage = messageService.getMessage(message.orElseThrow().getId());

        // then
        assertTrue(foundMessage.isPresent());
        assertEquals(message.orElseThrow().getId(), foundMessage.get().getId());
    }

    @Test
    @DisplayName("메시지를 생성하고 수정한 후 확인한다")
    void testUpdateMessage() {
        // given
        UUID authorID = UUID.randomUUID();
        UUID channelID = mockChannelService.createChannel("Test Channel").getId();
        Optional<Message> message = messageService.createMessage(authorID, channelID, "Hello, world!");
        String updatedText = "Updated";

        // when
        Optional<Message> updatedMessage = messageService.updateMessage(message.orElseThrow().getId(), updatedText);

        // then
        assertTrue(updatedMessage.isPresent());
        assertEquals(updatedText, updatedMessage.get().getText());

        Optional<Message> foundMessage = messageService.getMessage(message.orElseThrow().getId());
        assertTrue(foundMessage.isPresent());
        assertEquals(updatedText, foundMessage.get().getText());
    }

    @Test
    @DisplayName("메시지를 생성하고 삭제한 후 확인한다")
    void testDeleteMessage() {
        // given
        UUID authorID = UUID.randomUUID();
        UUID channelID = mockChannelService.createChannel("Test Channel").getId();
        Optional<Message> message = messageService.createMessage(authorID, channelID, "Hello, world!");

        // when
        Optional<Message> deletedMessage = messageService.deleteMessage(message.orElseThrow().getId());

        // then
        assertTrue(deletedMessage.isPresent());
        assertEquals(message.orElseThrow().getId(), deletedMessage.get().getId());
        assertFalse(messageService.getMessage(message.orElseThrow().getId()).isPresent());
    }

    @Test
    @DisplayName("메시지를 생성하고 새로운 인스턴스에서 로드가 되는지 확인한다")
    void testPersistenceAcrossInstances() {
        // given
        UUID authorID = UUID.randomUUID();
        UUID channelID = mockChannelService.createChannel("Persistent Channel").getId();
        Optional<Message> message = messageService.createMessage(authorID, channelID, "Persistent message");

        // when
        MessageService newServiceInstance = new FileMessageService(TEST_FILE_PATH, mockChannelService);

        // then
        Optional<Message> foundMessage = newServiceInstance.getMessage(message.orElseThrow().getId());
        assertTrue(foundMessage.isPresent());
        assertEquals(message.orElseThrow().getText(), foundMessage.get().getText());
    }
}
