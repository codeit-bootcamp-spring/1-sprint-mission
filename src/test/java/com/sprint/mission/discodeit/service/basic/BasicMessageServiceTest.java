package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
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
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

  @InjectMocks
  private BasicMessageService basicMessageService;

  @Mock
  private MessageRepository messageRepository;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private MessageMapper messageMapper;
  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private PageResponseMapper pageResponseMapper;

  @Test
  void create_Success() {
    // given
    UUID channelId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("content", channelId, userId);
    List<BinaryContentCreateRequest> binaryContentCreateRequests = new ArrayList<>();
    Channel channel = new Channel(ChannelType.PUBLIC, "name", "desc");
    User user = new User("test", "test@gmail.com", "qwer1234!", null);
    Message message = new Message(request.content(), channel, user, new ArrayList<>());
    MessageDto messageDto = new MessageDto(message.getId(), message.getCreatedAt(),
        message.getUpdatedAt(), message.getContent(), message.getChannel().getId(), null,
        new ArrayList<>());

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(messageRepository.save(any(Message.class))).willReturn(message);
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

    // when
    MessageDto result = basicMessageService.create(request, binaryContentCreateRequests);

    // then
    assertNotNull(result);
    assertEquals(result.content(), messageDto.content());
    then(messageRepository).should().save(any(Message.class));
  }

  @Test
  void create_Fail_ChannelNotFound() {
    // given
    UUID channelId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("content", channelId, userId);
    List<BinaryContentCreateRequest> binaryContentCreateRequests = new ArrayList<>();

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when, then
    assertThrows(ChannelNotFoundException.class,
        () -> basicMessageService.create(request, binaryContentCreateRequests));
  }

  @Test
  void create_Fail_UserNotFound() {
    // given
    UUID channelId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("content", channelId, userId);
    List<BinaryContentCreateRequest> binaryContentCreateRequests = new ArrayList<>();
    Channel channel = new Channel(ChannelType.PUBLIC, "name", "desc");

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // when, then
    assertThrows(UserNotFoundException.class,
        () -> basicMessageService.create(request, binaryContentCreateRequests));
  }

  @Test
  void findAllByChannelId_Success() {
    // given
    UUID channelId = UUID.randomUUID();
    Instant createAt = Instant.now().minusSeconds(3600); // 1 hour ago
    Pageable pageable = PageRequest.of(0, 10); // first page, 10 items

    // Create mock message entities
    Message message1 = new Message("message1", null, null, null);
    Message message2 = new Message("message2", null, null, null);
    List<Message> messages = List.of(message1, message2);

    // Mock the repository to return a slice of messages
    Slice<Message> messageSlice = new PageImpl<>(messages, pageable, messages.size());
    given(messageRepository.findAllByChannelIdWithAuthor(channelId, createAt, pageable))
        .willReturn(messageSlice);

    // Create mock MessageDto objects
    MessageDto messageDto1 = new MessageDto(UUID.randomUUID(), message1.getCreatedAt(),
        message1.getUpdatedAt(), message1.getContent(), null, null, null);
    MessageDto messageDto2 = new MessageDto(UUID.randomUUID(), message2.getCreatedAt(),
        message2.getUpdatedAt(), message2.getContent(), null, null, null);

    List<MessageDto> messageDtos = List.of(messageDto1, messageDto2);
    Slice<MessageDto> messageDtoSlice = new PageImpl<>(messageDtos, pageable, messages.size());

    // Mock the mapper to convert messages to MessageDtos
    given(messageMapper.toDto(message1)).willReturn(messageDto1);
    given(messageMapper.toDto(message2)).willReturn(messageDto2);

    // Mock the pageResponseMapper to map from slice to PageResponse
    given(pageResponseMapper.fromSlice(messageDtoSlice, null))
        .willReturn(new PageResponse<>(messageDtos, null, messageDtos.size(), false,
            (long) messages.size()));

    // when
    PageResponse<MessageDto> result = basicMessageService.findAllByChannelId(channelId, createAt,
        pageable);

    // then
    assertNotNull(result);
  }


  @Test
  void update() {
    // given
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("newContent");
    Message message = new Message("content", null, null, null);
    MessageDto messageDto = new MessageDto(messageId, message.getCreatedAt(),
        message.getUpdatedAt(), request.newContent(), null, null, null);

    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    given(messageRepository.save(any(Message.class))).willReturn(message);
    given(messageMapper.toDto(message)).willReturn(messageDto);

    // when
    MessageDto result = basicMessageService.update(messageId, request);

    // then
    assertNotNull(result);
    assertEquals(result.content(), messageDto.content());
    then(messageRepository).should().save(any(Message.class));
  }

  @Test
  void update_Fail_MessageNotFound() {
    //given
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("newContent");
    Message message = new Message("content", null, null, null);

    given(messageRepository.findById(messageId)).willReturn(Optional.empty());

    // when, then
    assertThrows(MessageNotFoundException.class,
        () -> basicMessageService.update(messageId, request));
  }

  @Test
  void delete_Success() {
    // given
    UUID messageId = UUID.randomUUID();
    given(messageRepository.existsById(messageId)).willReturn(true);

    // when
    basicMessageService.delete(messageId);

    // then
    then(messageRepository).should().deleteById(messageId);
  }

  @Test
  void delete_Fail_MessageNotFound() {
    // given
    UUID messageId = UUID.randomUUID();
    given(messageRepository.existsById(messageId)).willReturn(false);

    // when, then
    assertThrows(MessageNotFoundException.class, () -> basicMessageService.delete(messageId));
  }
}