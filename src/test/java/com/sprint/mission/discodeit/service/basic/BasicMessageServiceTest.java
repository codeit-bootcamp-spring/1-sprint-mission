package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.validation.ValidateBinaryContent;
import com.sprint.mission.discodeit.validation.ValidateMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

    @InjectMocks
    BasicMessageService basicMessageService;

    @Mock
    MessageRepository messageRepository;

    @Mock
    BinaryContentRepository binaryContentRepository;

    @Mock
    ValidateMessage validateMessage;

    @Test
    void create_Success() {
        // given
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        List<byte[]> contents = new ArrayList<>(List.of(
                new byte[]{1, 2, 3},
                new byte[]{4, 5, 6}
        ));
        MessageCreateRequest request = new MessageCreateRequest(userId, channelId, "안녕하세요.", contents);
        Message message = new Message(request.message(), request.userId(), request.channelId(), request.content());
        BinaryContent binaryContent = new BinaryContent(message.getAuthorId(), message.getId(), new byte[]{1, 2, 3});

        // when
        doNothing().when(validateMessage).validateMessage(request.message(), request.userId(), request.channelId());
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(binaryContent);

        MessageDto result = basicMessageService.create(request);

        // then
        assertNotNull(result);
        assertEquals(result.userId(), request.userId());
        assertEquals(result.channelId(), request.channelId());
        assertEquals(result.message(), request.message());
        assertEquals(result.content(), request.content());

        verify(messageRepository, times(1)).save(any(Message.class));
        verify(binaryContentRepository, times(2)).save(any(BinaryContent.class));
    }

    @Test
    void findAllByChannelId_Success() {
        // given
        UUID channelId = UUID.randomUUID();
        List<Message> messages = new ArrayList<>(List.of(
                new Message("안녕하세요.", UUID.randomUUID(), channelId, new ArrayList<>(List.of(new byte[]{1, 2, 3}, new byte[]{4, 5, 6}))),
                new Message("반갑습니다.", UUID.randomUUID(), channelId, new ArrayList<>(List.of(new byte[]{7, 8, 9}, new byte[]{1, 2, 4})))
        ));

        // when
        when(messageRepository.findByChannelId(channelId)).thenReturn(messages);

        List<MessageDto> results = basicMessageService.findAllByChannelId(channelId);

        // then
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(results.get(0).message(), messages.get(0).getMessage());
        assertEquals(results.get(1).message(), messages.get(1).getMessage());
        assertEquals(results.get(0).userId(), messages.get(0).getAuthorId());
        assertEquals(results.get(1).userId(), messages.get(1).getAuthorId());
        assertEquals(results.get(0).content(), messages.get(0).getContent());
        assertEquals(results.get(1).content(), messages.get(1).getContent());

        verify(messageRepository, times(1)).findByChannelId(channelId);
    }

    @Test
    void update_Success() {
        // given
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        List<byte[]> messages = new ArrayList<>(List.of(
                new byte[]{1, 2, 3},
                new byte[]{4, 5, 6}
        ));
        Message message = new Message("안녕하세요.", userId, channelId, messages);
        BinaryContent binaryContent = new BinaryContent(userId, message.getId(), new byte[]{1, 2, 3});
        MessageUpdateRequest request = new MessageUpdateRequest(message.getId(), userId, channelId, "수정 메시지", messages);

        // when
        doNothing().when(validateMessage).validateMessage(request.message(), request.userId(),request.channelId());
        when(messageRepository.findByMessageId(message.getId())).thenReturn(Optional.of(message));
        when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(binaryContent);
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        MessageDto result = basicMessageService.update(request);

        // then
        assertNotNull(result);
        assertEquals(result.message(), request.message());
        assertEquals(result.userId(), request.userId());
        assertEquals(result.channelId(), request.channelId());
        assertEquals(result.content(), request.content());

        verify(messageRepository, times(1)).findByMessageId(message.getId());
        verify(binaryContentRepository, times(1)).deleteByMessageId(message.getId());
        verify(binaryContentRepository, times(2)).save(any(BinaryContent.class));
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void update_MessageNotFound(){
        // given
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        List<byte[]> messages = new ArrayList<>(List.of(
                new byte[]{1, 2, 3},
                new byte[]{4, 5, 6}
        ));
        Message message = new Message("안녕하세요.", userId, channelId, messages);
        MessageUpdateRequest request = new MessageUpdateRequest(message.getId(), userId, channelId, "수정 메시지", messages);

        // when
        doNothing().when(validateMessage).validateMessage(request.message(), request.userId(),request.channelId());
        when(messageRepository.findByMessageId(message.getId())).thenReturn(Optional.empty());

        // then
        assertThrows(ResourceNotFoundException.class, () -> basicMessageService.update(request));

        verify(binaryContentRepository, never()).deleteByMessageId(message.getId());
        verify(binaryContentRepository, never()).save(any(BinaryContent.class));
        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    void delete_Success() {
        // given
        UUID messageID = UUID.randomUUID();

        // when
        basicMessageService.delete(messageID);

        // then
        verify(binaryContentRepository, times(1)).deleteByMessageId(messageID);
        verify(messageRepository, times(1)).deleteByMessageId(messageID);
    }
}