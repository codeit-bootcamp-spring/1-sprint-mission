package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.Type;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.BinaryContentMapperImpl;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

  @Mock
  UserRepository userRepository;
  @Mock
  ChannelRepository channelRepository;
  @Mock
  MessageRepository messageRepository;
  @Mock
  ReadStatusRepository readStatusRepository;
  @Mock
  BinaryContentRepository binaryContentRepository;
  @Mock
  BinaryContentStorage binaryContentStorage;
  @Spy
  PageResponseMapper pageResponseMapper;
  @Mock
  MessageMapper messageMapper;
  @InjectMocks
  MessageService messageService;

  @Spy
  BinaryContentMapper binaryContentMapper = new BinaryContentMapperImpl();

  @Test
  @DisplayName("message 생성 - attachment 없는 경우")
  void createMessage() {
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    String content = "message";
    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(authorId, content,
        channelId);
    User user = User.create("user", "user", "password");
    Channel channel = Channel.create(Type.PUBLIC, "public", "public");
    Message message = Message.create(user, content, channel, List.of());
    ReflectionTestUtils.setField(user, "id", authorId);
    ReflectionTestUtils.setField(channel, "id", channelId);
    ReflectionTestUtils.setField(message, "id", messageId);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.of(user));
    given(messageRepository.save(any())).willReturn(message);

    MessageDto messageDto = new MessageDto(message.getId(), message.getCreatedAt(),
        message.getUpdatedAt(), message.getContent(),
        new UserDto(user.getId(), user.getUsername(), user.getUsername(), null, false),
        message.getChannel().getId(), List.of());
    given(messageMapper.toDto(message)).willReturn(messageDto);

    MessageDto response = messageService.createMessage(messageCreateRequest, null);

    assertThat(response.id()).isEqualTo(messageDto.id());
    assertThat(response.content()).isEqualTo(messageDto.content());
    assertThat(response.channelId()).isEqualTo(messageDto.channelId());
    assertThat(response.author()).isEqualTo(messageDto.author());
  }

  @Test
  @DisplayName("message 생성 - attachment 있는 경우")
  void createMessageWithAttachment() throws IOException {
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID messageId = UUID.randomUUID();
    String content = "message";
    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(authorId, content,
        channelId);
    User user = User.create("user", "user", "password");
    Channel channel = Channel.create(Type.PUBLIC, "public", "public");
    ReflectionTestUtils.setField(user, "id", authorId);
    ReflectionTestUtils.setField(channel, "id", channelId);

    MultipartFile file = mock(MultipartFile.class);
    given(file.getSize()).willReturn(1024L);
    given(file.getOriginalFilename()).willReturn("profile.jpg");
    given(file.getBytes()).willReturn("new byte[]".getBytes());
    BinaryContent binaryContent = BinaryContent
        .create(file.getSize(), file.getOriginalFilename(), file.getContentType());

    Message message = Message.create(user, content, channel, List.of(binaryContent));
    ReflectionTestUtils.setField(message, "id", messageId);

    List<BinaryContentDto> attachments = message.getAttachments().stream()
        .map(binaryContentMapper::toDto)
        .toList();

    given(binaryContentRepository.save(any())).willReturn(binaryContent);
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.of(user));
    given(messageRepository.save(any())).willReturn(message);

    MessageDto messageDto = new MessageDto(message.getId(), message.getCreatedAt(),
        message.getUpdatedAt(), message.getContent(),
        new UserDto(user.getId(), user.getUsername(), user.getUsername(), null, false),
        message.getChannel().getId(), attachments);
    given(messageMapper.toDto(message)).willReturn(messageDto);

    MessageDto response = messageService.createMessage(messageCreateRequest, List.of(file));

    assertThat(response).isEqualTo(messageDto);

    verify(binaryContentStorage).put(any(), any());
    verify(binaryContentRepository).save(any());
  }

  @Test
  @DisplayName("message 생성 실패 - channel이 없는 경우")
  void failCreateMessageChannel() {
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    String content = "message";
    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(authorId, content,
        channelId);

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.createMessage(messageCreateRequest, null))
        .isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  @DisplayName("message 생성 실패 - user가 없는 경우")
  void failCreateMessageUser() {
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    String content = "message";
    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(authorId, content,
        channelId);

    given(channelRepository.findById(channelId)).willReturn(
        Optional.of(Channel.create(Type.PUBLIC, "public", "public")));
    given(userRepository.findById(authorId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.createMessage(messageCreateRequest, null))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @DisplayName("message 생성 실패 - channel에 등록되지 않은 경우")
  void failCreateMessageUnregistered() {
    UUID authorId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    String content = "message";
    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(authorId, content,
        channelId);

    given(channelRepository.findById(channelId))
        .willReturn(Optional.of(Channel.create(Type.PRIVATE, "public", "public")));
    given(userRepository.findById(authorId))
        .willReturn(Optional.of(User.create("username", "email", "password")));
    given(readStatusRepository.existsByUser_IdAndChannel_Id(authorId, channelId))
        .willReturn(false);

    assertThatThrownBy(() -> messageService.createMessage(messageCreateRequest, null))
        .isInstanceOf(ReadStatusNotFoundException.class);
  }

  @Test
  @DisplayName("message 조회 - cursor가 null인 경우")
  void readMessage() {
    UUID channelId = UUID.randomUUID();
    Pageable pageable = PageRequest.of(0, 10);
    Instant createdAt = Instant.now();
    MessageDto messageDto = new MessageDto(
        UUID.randomUUID(),
        createdAt,
        createdAt,
        "message",
        mock(UserDto.class),
        channelId,
        List.of()
    );

    given(messageRepository.findPageByChannelId(channelId, pageable))
        .willReturn(new SliceImpl<>(List.of(mock(Message.class)), pageable, false));
    given(messageMapper.toDto(any())).willReturn(messageDto);

    PageResponse<MessageDto> pageResponse =
        messageService.readAllByChannelId(channelId, null, pageable);

    assertThat(pageResponse.content().get(0)).isEqualTo(messageDto);
    assertThat(pageResponse.size()).isEqualTo(pageable.getPageSize());
    assertThat(pageResponse.hasNext()).isFalse();
    assertThat(pageResponse.nextCursor()).isEqualTo(createdAt);
    assertThat(pageResponse.totalElements()).isNull();

    verify(messageRepository, never()).findPageByChannelIdWithCursor(any(), any(), any());
  }

  @Test
  @DisplayName("message 조회 - cursor가 존재하는 경우")
  void readMessageWithCursor() {
    UUID channelId = UUID.randomUUID();
    Pageable pageable = PageRequest.of(0, 10);
    Instant cursor = Instant.parse("2024-04-02T00:00:00Z");
    Message message1 = mock(Message.class);
    MessageDto messageDto1 = new MessageDto(
        UUID.randomUUID(),
        Instant.parse("2024-04-01T00:00:00Z"),
        Instant.parse("2024-04-01T00:00:00Z"),
        "message1",
        mock(UserDto.class),
        channelId,
        List.of()
    );
    MessageDto messageDto2 = new MessageDto(
        UUID.randomUUID(),
        Instant.parse("2024-04-03T00:00:00Z"),
        Instant.parse("2024-04-03T00:00:00Z"),
        "message2",
        mock(UserDto.class),
        channelId,
        List.of()
    );

    given(messageRepository.findPageByChannelIdWithCursor(channelId, cursor, pageable))
        .willReturn(new SliceImpl<>(List.of(message1), pageable, false));
    given(messageMapper.toDto(message1)).willReturn(messageDto1);

    PageResponse<MessageDto> pageResponse =
        messageService.readAllByChannelId(channelId, cursor, pageable);

    assertThat(pageResponse.content().get(0)).isEqualTo(messageDto1);
    assertThat(pageResponse.size()).isEqualTo(pageable.getPageSize());
    assertThat(pageResponse.hasNext()).isFalse();
    assertThat(pageResponse.nextCursor()).isEqualTo(messageDto1.createdAt());
    assertThat(pageResponse.totalElements()).isNull();

    verify(messageRepository, never()).findPageByChannelId(any(), any());
  }

  @Test
  @DisplayName("message 수정")
  void updateMessage() {
    Message message = Message.create(mock(User.class), "message", mock(Channel.class), List.of());

    UUID messageId = UUID.randomUUID();
    String newContent = "new message";

    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    given(messageMapper.toDto(message)).willAnswer(i ->
        new MessageDto(messageId, message.getCreatedAt(), message.getUpdatedAt(),
            message.getContent(), null, null, null));

    MessageDto messageDto = messageService.updateMessage(messageId, newContent);

    assertThat(messageDto.content()).isEqualTo(newContent);
  }

  @Test
  @DisplayName("message 수정 실패 - 존재하지 않는 경우")
  void failUpdateMessage() {
    UUID messageId = UUID.randomUUID();
    String newContent = "new message";

    given(messageRepository.findById(messageId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.updateMessage(messageId, newContent))
        .isInstanceOf(MessageNotFoundException.class);

    verify(messageRepository, never()).save(any());
  }

  @Test
  @DisplayName("message 삭제 - 존재하지 않는 경우")
  void deleteMessage() {
    UUID messageId = UUID.randomUUID();

    given(messageRepository.findByIdWithAttachments(messageId)).willReturn(Optional.empty());

    messageService.deleteMessage(messageId);

    verify(messageRepository, never()).delete(any());
    verify(binaryContentStorage, never()).delete(any());
  }

  @Test
  @DisplayName("message 삭제 - 존재하는 경우")
  void deleteExistMessage() {
    UUID messageId = UUID.randomUUID();
    given(messageRepository.findByIdWithAttachments(messageId))
        .willReturn(Optional.of(mock(Message.class)));

    messageService.deleteMessage(messageId);

    verify(messageRepository).delete(any());
  }
}