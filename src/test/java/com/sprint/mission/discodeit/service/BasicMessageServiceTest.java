package com.sprint.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;

import com.sprint.mission.discodeit.dto.binary.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.ReadStatusService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReadStatusService readStatusService;
    @Mock
    private PageResponseMapper pageResponseMapper;
    @Mock
    private MessageMapper messageMapper;
    @InjectMocks
    private BasicMessageService basicMessageService;

    private Field getFieldRecursively(Class<?> clazz, String fieldName)
            throws NoSuchFieldException {
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName + " not found in class hierarchy");
    }

    private void setId(Object target, UUID id) throws Exception {
        Field field = getFieldRecursively(target.getClass(), "id");
        field.setAccessible(true);
        field.set(target, id);
    }

    private void setCreatedAt(Object target, Instant time) throws Exception {
        Field field = getFieldRecursively(target.getClass(), "createdAt");
        field.setAccessible(true);
        field.set(target, time);
    }

    @Test
    void 메시지_생성_성공() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        UUID messageId = UUID.randomUUID();

        Channel channel = new Channel(ChannelType.PUBLIC, "테스트 채널", null);
        User user = new User("user", "user@naver.com", "1234", null);
        setId(user, userId);
        setId(channel, channelId);

        CreateMessageRequestDto request = new CreateMessageRequestDto("테스트", channelId, userId);
        Message message = new Message("테스트 메시지", channel, user, List.of());
        setId(message, messageId);

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(messageRepository.save(any(Message.class))).willReturn(message);
        given(messageMapper.toDto(any(Message.class))).willReturn(new MessageDto());

        MessageDto result = basicMessageService.createMessage(request, null);

        assertNotNull(result);
        verify(messageRepository).save(any(Message.class));
        verify(channelRepository).findById(channelId);
        verify(userRepository).findById(userId);
    }

    @Test
    void 메시지_생성_실패_채널없음() {
        UUID userId = UUID.randomUUID();
        CreateMessageRequestDto request = new CreateMessageRequestDto("테스트", null, userId);
        given(channelRepository.findById(null)).willReturn(Optional.empty());
        assertThrows(ChannelNotFoundException.class,
                () -> basicMessageService.createMessage(request,
                        (List<BinaryContentCreateRequest>) null));
    }

    @Test
    void 메시지_생성_실패_작성자없음() {
        UUID channelId = UUID.randomUUID();
        CreateMessageRequestDto request = new CreateMessageRequestDto("테스트", channelId, null);

        Channel dummyChannel = new Channel(ChannelType.PUBLIC, "더미 채널", null);
        given(channelRepository.findById(channelId)).willReturn(Optional.of(dummyChannel));
        given(userRepository.findById(null)).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> basicMessageService.createMessage(request,
                        (List<BinaryContentCreateRequest>) null);
    }

    @Test
    void 메시지_조회_첫페이지() throws Exception {
        UUID channelId = UUID.randomUUID();
        int size = 2;
        Message m1 = new Message("msg1", null, null, List.of());
        Message m2 = new Message("msg2", null, null, List.of());
        Instant t1 = Instant.now().minusSeconds(30);
        Instant t2 = Instant.now().minusSeconds(60);
        setCreatedAt(m1, t1);
        setCreatedAt(m2, t2);

        Slice<Message> messages = new SliceImpl<>(List.of(m1, m2), PageRequest.of(0, size), true);
        MessageDto dto1 = new MessageDto();
        dto1.setContent("msg1");
        MessageDto dto2 = new MessageDto();
        dto2.setContent("msg2");

        given(messageRepository.findFirstMessages(eq(channelId), any(Pageable.class))).willReturn(
                messages);
        given(messageMapper.toDto(m1)).willReturn(dto1);
        given(messageMapper.toDto(m2)).willReturn(dto2);
        given(pageResponseMapper.fromSlice(any(), eq(t2)))
                .willReturn(new PageResponse<>(List.of(dto1, dto2), t2, size, true, null));

        PageResponse<MessageDto> result = basicMessageService.findAllByChannelId(channelId, null,
                size);

        assertEquals(2, result.getContent().size());
        assertEquals("msg1", result.getContent().get(0).getContent());
        assertEquals("msg2", result.getContent().get(1).getContent());
        assertTrue(result.isHasNext());
        assertEquals(t2, result.getNextCursor());
    }

    @Test
    void 메시지_조회_다음페이지() throws Exception {
        UUID channelId = UUID.randomUUID();
        int size = 1;
        Instant cursor = Instant.now().minusSeconds(10);
        Instant time = cursor.minusSeconds(30);

        Message m = new Message("msg-next", null, null, List.of());
        setCreatedAt(m, time);
        MessageDto dto = new MessageDto();
        dto.setContent("msg-next");
        Slice<Message> slice = new SliceImpl<>(List.of(m), PageRequest.of(0, size), true);

        given(messageRepository.findNextMessages(eq(channelId), eq(cursor),
                any(Pageable.class))).willReturn(slice);
        given(messageMapper.toDto(m)).willReturn(dto);
        given(pageResponseMapper.fromSlice(any(), eq(time)))
                .willReturn(new PageResponse<>(List.of(dto), time, size, true, null));

        PageResponse<MessageDto> result = basicMessageService.findAllByChannelId(channelId, cursor,
                size);

        assertEquals(1, result.getContent().size());
        assertEquals("msg-next", result.getContent().get(0).getContent());
        assertTrue(result.isHasNext());
        assertEquals(time, result.getNextCursor());
    }

    @Test
    void 메시지_수정_성공() throws Exception {
        UUID id = UUID.randomUUID();
        User u = new User("u", "u@e.com", "p", null);
        Channel c = new Channel(ChannelType.PUBLIC, "c", null);
        Message m = new Message("old", c, u, List.of());
        setId(m, id);
        UpdateMessageRequestDto req = new UpdateMessageRequestDto("new");
        MessageDto dto = new MessageDto();
        dto.setContent("new");

        given(messageRepository.findById(id)).willReturn(Optional.of(m));
        given(messageMapper.toDto(m)).willReturn(dto);

        MessageDto result = basicMessageService.updateMessage(id, req);

        assertEquals("new", result.getContent());
        then(messageRepository).should().findById(id);
        then(messageMapper).should().toDto(m);
        verifyNoMoreInteractions(messageRepository, messageMapper);
    }

    @Test
    void 메시지_수정_실패_없음() {
        UUID id = UUID.randomUUID();
        given(messageRepository.findById(id)).willReturn(Optional.empty());
        assertThrows(MessageNotFoundException.class,
                () -> basicMessageService.updateMessage(id, new UpdateMessageRequestDto("n")));
    }

    @Test
    void 메시지_삭제_성공() {
        UUID id = UUID.randomUUID();
        given(messageRepository.existsById(id)).willReturn(true);
        basicMessageService.deleteMessage(id);
        then(messageRepository).should().existsById(id);
        then(messageRepository).should().deleteById(id);
    }

    @Test
    void 메시지_삭제_실패_없음() {
        UUID id = UUID.randomUUID();
        given(messageRepository.existsById(id)).willReturn(false);
        assertThrows(MessageNotFoundException.class, () -> basicMessageService.deleteMessage(id));
    }
}