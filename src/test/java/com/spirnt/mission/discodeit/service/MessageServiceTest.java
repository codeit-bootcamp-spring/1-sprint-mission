package com.spirnt.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageDto;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.dto.response.PageResponse;
import com.spirnt.mission.discodeit.entity.Channel;
import com.spirnt.mission.discodeit.entity.ChannelType;
import com.spirnt.mission.discodeit.entity.Message;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.spirnt.mission.discodeit.exception.Message.MessageNotFoundException;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.mapper.MessageMapper;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.basic.BasicMessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @InjectMocks
    private BasicMessageService basicMessageService;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private UserStatusService userStatusService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("메시지 생성 테스트 - 성공")
    void testCreateMessageSuccess() {
        // given
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        User user = new User("username", "email", "password");
        ReflectionTestUtils.setField(user, "id", userId);
        Channel channel = new Channel("channelname", null, ChannelType.PUBLIC);
        ReflectionTestUtils.setField(channel, "id", channelId);
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest("content", userId,
            channelId);
        Message message = new Message(messageCreateRequest.content(), user, channel,
            new ArrayList<>());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
        when(messageRepository.save(eq(message))).thenReturn(message);
        when(messageMapper.toDto(message)).thenReturn(
            new MessageDto(message.getId(), message.getCreatedAt(), message.getUpdatedAt(),
                message.getContent(), channelId, null, null));
        // when
        MessageDto result = basicMessageService.create(messageCreateRequest, null);
        // then
        assertEquals(result.getId(), message.getId());
        assertEquals(result.getContent(), message.getContent());
        assertEquals(result.getChannelId(), message.getChannel().getId());
        verify(userRepository).findById(userId);
        verify(channelRepository).findById(channelId);
        verify(messageRepository).save(message);
    }

    @Test
    @DisplayName("메시지 생성 테스트 - 실패: 유저가 존재하지 않음")
    void testCreateMessageFailDueToUserNotFound() {
        // given
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        MessageCreateRequest messageCreateRequest = new MessageCreateRequest("test", userId,
            channelId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        // when & then
        assertThrows(UserNotFoundException.class,
            () -> basicMessageService.create(messageCreateRequest, null));
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("메시지 수정 테스트 - 성공")
    void testUpdateMessageSuccess() {
        // given
        UUID messageId = UUID.randomUUID();
        MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest("newContent");
        Message message = new Message("originalContent", new User(), new Channel(), null);
        ReflectionTestUtils.setField(message, "id", messageId);
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageMapper.toDto(any(Message.class)))
            .thenAnswer(invocation -> {
                Message messageArg = invocation.getArgument(0);
                return new MessageDto(messageArg.getId(),
                    messageArg.getCreatedAt(),
                    messageArg.getUpdatedAt(),
                    messageArg.getContent(),
                    null, null, null);
            });
        // when
        MessageDto result = basicMessageService.update(messageId, messageUpdateRequest);
        // then
        assertEquals(result.getId(), messageId);
        assertEquals(result.getContent(), message.getContent());
        verify(messageRepository).findById(messageId);
    }

    @Test
    @DisplayName("메시지 수정 테스트 - 실패: 메시지가 존재하지 않음")
    void testUpdateMessageFailDueToMessageNotFound() {
        UUID messageId = UUID.randomUUID();
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
        // when & then
        assertThrows(MessageNotFoundException.class,
            () -> basicMessageService.update(messageId, new MessageUpdateRequest("newContent")));
        verify(messageRepository).findById(messageId);
    }

    @Test
    @DisplayName("메시지 삭제 테스트 - 성공")
    void testDeleteMessageSuccess() {
        // given
        UUID messageId = UUID.randomUUID();
        Message message = new Message("originalContent", new User(), new Channel(), null);
        ReflectionTestUtils.setField(message, "id", messageId);
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        // when
        basicMessageService.delete(messageId);
        // then
        verify(messageRepository).findById(messageId);
        verify(messageRepository).delete(message);
    }

    @Test
    @DisplayName("메시지 삭제 테스트 - 실패: 메시지가 존재하지 않음")
    void testDeleteMessageFailDueToMessageNotFound() {
        UUID messageId = UUID.randomUUID();
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
        // when & then
        assertThrows(MessageNotFoundException.class,
            () -> basicMessageService.delete(messageId));
        verify(messageRepository).findById(messageId);
    }

    @Test
    @DisplayName("채널 내 메시지 찾기 테스트 - 성공")
    void testFindMessagesSuccess() {
        // given
        UUID channelId = UUID.randomUUID();
        when(channelRepository.existsById(channelId)).thenReturn(true);
        Pageable pageable = PageRequest.of(0, 5);

        Message message1 = new Message("m1", new User(), new Channel(), null);
        Message message2 = new Message("m2", new User(), new Channel(), null);
        Slice<Message> messageSlice = new SliceImpl<>(List.of(message1, message2), pageable, false);
        when(messageRepository.findByChannelId(channelId, pageable)).thenReturn(messageSlice);
        when(messageMapper.toDto(any(Message.class)))
            .thenAnswer(invocation -> {
                Message arg = invocation.getArgument(0);
                return new MessageDto(arg.getId(), arg.getCreatedAt(), arg.getUpdatedAt(),
                    arg.getContent(), null, null, null);
            });

        // when
        PageResponse result = basicMessageService.findAllByChannelId(channelId, null, pageable);
        // then
        assertNotNull(result);
        assertEquals(result.getSize(), 2);
        verify(channelRepository).existsById(channelId);
        verify(messageRepository).findByChannelId(channelId, pageable);

    }

    @Test
    @DisplayName("채널 내 메시지 찾기 테스트 - 실패: 채널이 존재하지 않음")
    void testFindMessagesFailDueToChannelNotFound() {
        UUID channelId = UUID.randomUUID();
        when(channelRepository.existsById(channelId)).thenReturn(false);
        // when & then
        assertThrows(ChannelNotFoundException.class,
            () -> basicMessageService.findAllByChannelId(channelId, null, null));
        verify(channelRepository).existsById(channelId);
    }
}
