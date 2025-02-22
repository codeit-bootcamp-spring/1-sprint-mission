package com.sprint.mission.discodeit.service.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

class FileMessageServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChannelService channelService;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("메시지를 생성한다")
    void testCreateMessage() {
        // given
        UUID authorID = UUID.randomUUID();
        String channelName = "test_channel";
        Channel channel1 = new Channel(channelName);
        String text = "text";

        CreateMessageRequest request = new CreateMessageRequest(authorID, channel1.getId(), text);

        // when
        when(channelService.getChannel(any())).thenReturn(Optional.of(ChannelResponse.fromEntity(channel1)));
        Optional<MessageResponse> message = Optional.ofNullable(messageService.createMessage(request));

        // then
        assertNotNull(message);
        assertEquals(text, message.orElseThrow().text());
        assertEquals(authorID, message.orElseThrow().authorId());
        assertEquals(channel1.getId(), message.orElseThrow().channelId());
    }

    @Test
    @DisplayName("메시지를 생성하고 단일 조회를 한다")
    void testGetMessage() {
        // given
        UUID authorID = UUID.randomUUID();
        String channelName = "test_channel";
        Channel channel1 = new Channel(channelName);
        String text = "text";

        Message message1 = new Message(text, authorID, channel1.getId());
        CreateMessageRequest request = new CreateMessageRequest(authorID, channel1.getId(), text);

        // when
        when(channelService.getChannel(any())).thenReturn(Optional.of(ChannelResponse.fromEntity(channel1)));
        MessageResponse message = messageService.createMessage(request);
        when(messageRepository.getMessageById(any())).thenReturn(message1);
        Optional<MessageResponse> response = messageService.getMessage(message.id());

        // then
        assertTrue(response.isPresent());
        assertEquals(text, response.get().text());
        assertEquals(authorID, response.get().authorId());
        assertEquals(channel1.getId(), response.get().channelId());
    }

    @Test
    @DisplayName("메시지를 생성하고 수정한 후 확인한다")
    void testUpdateMessage() {
        // given
        UUID authorID = UUID.randomUUID();
        String channelName = "test_channel";
        Channel channel1 = new Channel(channelName);
        String text = "text";

        Message message1 = new Message(text, authorID, channel1.getId());
        CreateMessageRequest request = new CreateMessageRequest(authorID, channel1.getId(), text);

        // when
        when(channelService.getChannel(any())).thenReturn(Optional.of(ChannelResponse.fromEntity(channel1)));
        MessageResponse message = messageService.createMessage(request);
        when(messageRepository.getMessageById(any())).thenReturn(message1);
        Optional<MessageResponse> response = messageService.getMessage(message.id());

        // then
        assertTrue(response.isPresent());
        assertEquals(text, response.get().text());
        assertEquals(authorID, response.get().authorId());
        assertEquals(channel1.getId(), response.get().channelId());

        // when
        String updatedText = "updated";
        UpdateMessageRequest request1 = new UpdateMessageRequest(message.id(), updatedText);
        when(channelService.getChannel(any())).thenReturn(Optional.of(ChannelResponse.fromEntity(channel1)));
        when(messageRepository.getMessageById(any())).thenReturn(message1);
        Optional<MessageResponse> updatedResponse = messageService.updateMessage(request1);

        // then
        assertTrue(updatedResponse.isPresent());
        assertEquals(updatedText, updatedResponse.get().text());
        assertEquals(authorID, updatedResponse.get().authorId());
        assertEquals(channel1.getId(), updatedResponse.get().channelId());
    }

    @Test
    @DisplayName("메시지를 생성하고 삭제한 후 확인한다")
    void testDeleteMessage() {
        // given
        UUID authorID = UUID.randomUUID();
        String channelName = "test_channel";
        Channel channel1 = new Channel(channelName);
        String text = "text";

        Message message1 = new Message(text, authorID, channel1.getId());
        CreateMessageRequest request = new CreateMessageRequest(authorID, channel1.getId(), text);

        // when
        when(channelService.getChannel(any())).thenReturn(Optional.of(ChannelResponse.fromEntity(channel1)));
        MessageResponse message = messageService.createMessage(request);
        when(messageRepository.getMessageById(any())).thenReturn(message1);
        Optional<MessageResponse> response = messageService.getMessage(message.id());

        // then
        assertTrue(response.isPresent());
        assertEquals(text, response.get().text());
        assertEquals(authorID, response.get().authorId());
        assertEquals(channel1.getId(), response.get().channelId());

        // when
        messageService.deleteMessage(message.id());
        when(messageRepository.getMessageById(any())).thenReturn(null);
        response = messageService.getMessage(message.id());

        // then
        assertFalse(response.isPresent());
    }
}
