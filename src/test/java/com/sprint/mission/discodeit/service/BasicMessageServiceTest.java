package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
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

@ExtendWith(MockitoExtension.class)
public class BasicMessageServiceTest {

  @Mock
  MessageRepository messageRepository;

  @Mock
  ChannelRepository channelRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  BinaryContentRepository binaryContentRepository;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @Mock
  MessageMapper messageMapper;

  @Mock
  PageResponseMapper pageResponseMapper;

  @InjectMocks
  BasicMessageService messageService;

  @Test
  @DisplayName("메시지 생성 - 성공")
  void createMessageSuccess() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("내용", channelId, authorId);

    Channel channel = new Channel(ChannelType.PUBLIC, "채널", "설명");
    User author = new User("user", "user@email.com", "1234", null);
    BinaryContentCreateRequest file = new BinaryContentCreateRequest("file.txt", "text/plain",
        "파일 내용".getBytes());
    BinaryContent savedFile = new BinaryContent(file.fileName(), (long) file.bytes().length,
        file.contentType());

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.of(author));
    given(binaryContentRepository.save(any())).willReturn(savedFile);

    UUID messageId = UUID.randomUUID();
    MessageDto dto = new MessageDto(messageId, Instant.now(), null, "내용", null, null, null);

    given(messageMapper.toDto(any())).willReturn(dto);
    MessageDto result = messageService.create(request, List.of(file));

    assertThat(result).isNotNull();
    then(channelRepository).should().findById(channelId);
    then(userRepository).should().findById(authorId);
    then(messageRepository).should().save(any());
    then(messageMapper).should().toDto(any());
  }

  @Test
  @DisplayName("메시지 생성 실패 - 채널 없음")
  void createMessageFail() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("내용", channelId, authorId);

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.create(request, List.of()))
        .isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  @DisplayName("메시지 수정 - 성공")
  void updateMessageSuccess() {
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("수정된 내용");

    Message message = mock(Message.class);
    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    given(messageMapper.toDto(message)).willReturn(
        new MessageDto(messageId, Instant.now(), null, "수정된 내용", null, null, null));

    MessageDto result = messageService.update(messageId, request);

    assertThat(result.content()).isEqualTo("수정된 내용");
    then(messageRepository).should().findById(messageId);
    then(message).should().update("수정된 내용");
    then(messageMapper).should().toDto(message);
  }

  @Test
  @DisplayName("메시지 수정 실패 - 메시지 없음 예외")
  void updateMessageNotFound() {
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("수정 내용");

    given(messageRepository.findById(messageId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.update(messageId, request))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  @DisplayName("채널 메시지 목록 조회 - 성공")
  void findAllByChannelIdSuccess() {
    UUID channelId = UUID.randomUUID();
    Instant now = Instant.now();
    Pageable pageable = PageRequest.of(0, 10);

    User author = new User("username", "email@example.com", "password", null);
    Message message = new Message("Hello", new Channel(ChannelType.PUBLIC, "test", null), author,
        List.of());
    MessageDto dto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        null,
        "Hello",
        channelId,
        null,
        null
    );

    Slice<Message> messageSlice = new SliceImpl<>(List.of(message), pageable, false);

    given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(), eq(pageable)))
        .willReturn(messageSlice);
    given(messageMapper.toDto(any(Message.class))).willReturn(dto);
    given(pageResponseMapper.fromSlice(any(), any())).willReturn(
        new PageResponse<>(
            List.of(dto),
            null,
            1,
            false,
            1L
        )
    );

    PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, now, pageable);

    assertThat(result.content()).hasSize(1);
    assertThat(result.content().get(0).content()).isEqualTo("Hello");
  }

  @DisplayName("채널 메시지 목록 조회 실패 - 빈 결과")
  @Test
  void findAllByChannelIdFail() {
    UUID channelId = UUID.randomUUID();
    Pageable pageable = PageRequest.of(0, 5);
    Slice<Message> emptySlice = new SliceImpl<>(List.of(), pageable, false);

    given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(), eq(pageable)))
        .willReturn(emptySlice);
    given(pageResponseMapper.fromSlice(any(), any())).willReturn(
        new PageResponse<>(
            List.of(),
            null,
            0,
            false,
            0L
        )
    );

    PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, null, pageable);

    assertThat(result.content()).isEmpty();
    then(messageRepository).should()
        .findAllByChannelIdWithAuthor(eq(channelId), any(), eq(pageable));
    then(pageResponseMapper).should().fromSlice(any(), any());
  }
}