package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.eq;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
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
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private MessageMapper messageMapper;
  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private PageResponseMapper pageResponseMapper;

  @InjectMocks
  private BasicMessageService messageService;

  private UUID messageId;
  private UUID channelId;
  private UUID authorId;

  private MessageCreateRequest createRequest;
  private MessageUpdateRequest updateRequest;
  private MessageDto messageDto;

  private Channel channel;
  private User user;
  private Message message;

  @BeforeEach
  void setUp() {
    messageId = UUID.randomUUID();
    channelId = UUID.randomUUID();
    authorId = UUID.randomUUID();

    channel = new Channel(ChannelType.PUBLIC, "publicChannel", "desc");
    channel.setId(channelId);
    user = new User("testUser", "test@example.com", "password", null);
    user.setId(authorId);

    message = new Message("Hello world", channel, user, Collections.emptyList());
    message.setId(messageId);

    createRequest = new MessageCreateRequest("Hello world", channelId, authorId);
    updateRequest = new MessageUpdateRequest("Updated content");

    messageDto = new MessageDto(messageId, Instant.now(), null, "Hello world", channelId,
        new UserDto(authorId, "testUser", "test@example.com", null, true),
        Collections.emptyList()
    );
  }

  @Test
  @DisplayName("create 성공")
  void create_Success() {
    // Given
    List<BinaryContentCreateRequest> attachments = Collections.emptyList();
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.of(user));
    given(messageRepository.save(any(Message.class))).willReturn(message);
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

    // When
    MessageDto result = messageService.create(createRequest, attachments);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(messageId);
    assertThat(result.content()).isEqualTo("Hello world");
    then(channelRepository).should().findById(channelId);
    then(userRepository).should().findById(authorId);
    then(messageRepository).should().save(any(Message.class));
  }

  @Test
  @DisplayName("create 실패 - 채널이 존재하지 않음")
  void create_Fail_ChannelNotFound() {
    // Given
    List<BinaryContentCreateRequest> attachments = Collections.emptyList();
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> messageService.create(createRequest, attachments))
        .isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  @DisplayName("create 실패 - 유저가 존재하지 않음")
  void create_Fail_UserNotFound() {
    // Given
    List<BinaryContentCreateRequest> attachments = Collections.emptyList();
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> messageService.create(createRequest, attachments))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void findAllByChannelId_Success() {
    // Given
    Instant cursor = Instant.now();
    PageRequest pageRequest = PageRequest.of(0, 10);

    SliceImpl<Message> entitySlice = new SliceImpl<>(List.of(message), pageRequest, false);
    given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(Instant.class), eq(pageRequest))).willReturn(entitySlice);

    Slice<MessageDto> dtoSlice = new SliceImpl<>(List.of(messageDto), pageRequest, false);
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

    // 3) mock pageResponseMapper
    PageResponse<MessageDto> response = new PageResponse<>(List.of(messageDto), null, 1, false, 1L);
    given(pageResponseMapper.fromSlice(eq(dtoSlice), any())).willReturn(response);

    // When
    PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, cursor, pageRequest);

    // Then
    assertThat(result.content()).hasSize(1);
    assertThat(result.content().get(0).id()).isEqualTo(messageId);
  }


  @Test
  @DisplayName("findAllByChannelId 성공 - 메시지 없음")
  void findAllByChannelId_Empty() {
    // Given
    Instant cursor = Instant.now();
    PageRequest pageRequest = PageRequest.of(0, 10);

    SliceImpl<Message> entityEmptySlice = new SliceImpl<>(Collections.emptyList(), pageRequest, false);
    given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(Instant.class), eq(pageRequest)))
        .willReturn(entityEmptySlice);

    SliceImpl<MessageDto> dtoEmptySlice = new SliceImpl<>(Collections.emptyList(), pageRequest, false);
    PageResponse<MessageDto> emptyResponse = new PageResponse<>(Collections.emptyList(), null, 0,false,0L);
    given(pageResponseMapper.fromSlice(eq(dtoEmptySlice), eq(null))).willReturn(emptyResponse);

    // When
    PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, cursor, pageRequest);

    // Then
    assertThat(result.content()).isEmpty();
  }


  @Test
  @DisplayName("update 성공")
  void update_Success() {
    // Given
    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

    // When
    MessageDto result = messageService.update(messageId, updateRequest);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.content()).isEqualTo("Hello world");
    then(messageRepository).should().findById(messageId);
  }

  @Test
  @DisplayName("update 실패 - 메시지 존재하지 않음")
  void update_Fail_MessageNotFound() {
    // Given
    given(messageRepository.findById(messageId)).willReturn(Optional.empty());
    // When & Then
    assertThatThrownBy(() -> messageService.update(messageId, updateRequest))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  @DisplayName("delete 성공")
  void delete_Success() {
    // Given
    given(messageRepository.existsById(messageId)).willReturn(true);
    willDoNothing().given(messageRepository).deleteById(messageId);

    // When
    messageService.delete(messageId);

    // Then
    then(messageRepository).should().existsById(messageId);
    then(messageRepository).should().deleteById(messageId);
  }

  @Test
  @DisplayName("delete 실패 - 메시지 존재하지 않음")
  void delete_Fail_MessageNotFound() {
    // Given
    given(messageRepository.existsById(messageId)).willReturn(false);

    // When & Then
    assertThatThrownBy(() -> messageService.delete(messageId))
        .isInstanceOf(MessageNotFoundException.class);
  }
}
