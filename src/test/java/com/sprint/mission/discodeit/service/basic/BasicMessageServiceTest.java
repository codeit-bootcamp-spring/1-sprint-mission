package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotfoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private MessageMapper messageMapper;

  @Mock
  private BinaryContentService binaryContentService;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private PageResponseMapper pageResponseMapper;

  @InjectMocks
  private BasicMessageService messageService;

  @Nested
  @DisplayName("메시지 생성")
  class CreateTest {

    @Test
    @DisplayName("메시지 생성 성공")
    void create_message_success() {
      // given
      UUID authorId = UUID.randomUUID();
      User author = new User("홍길동", "hong@codeit.com", "hong1234", null);
      ReflectionTestUtils.setField(author, "id", authorId);

      UUID channelId = UUID.randomUUID();
      Channel channel = new Channel(Channel.ChannelType.PUBLIC, "일반채널", "설명");
      ReflectionTestUtils.setField(channel, "id", channelId);

      Message message = new Message("Hello", channel, author, Collections.emptyList());
      ReflectionTestUtils.setField(message, "id", UUID.randomUUID());

      given(userRepository.findById(any())).willReturn(Optional.of(author));
      given(channelRepository.findById(any())).willReturn(Optional.of(channel));
      given(messageRepository.save(any(Message.class))).willReturn(message);

      MessageDto messageDto = new MessageDto(
          message.getId(),
          Instant.now(),
          Instant.now(),
          "Hello",
          channelId,
          null,
          null
      );
      given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

      MessageCreateRequest request = new MessageCreateRequest("Hello", channelId, authorId);

      // when
      MessageDto result = messageService.create(request, null);

      // then
      then(messageRepository).should().save(any(Message.class));
      then(messageMapper).should().toDto(any(Message.class));
      assertThat(result.content()).isEqualTo("Hello");
    }

    @Test
    @DisplayName("메시지 생성 실패 - user not found")
    void create_message_failure_when_user_not_found() {
      // given
      UUID authorId = UUID.randomUUID();
      UUID channelId = UUID.randomUUID();

      given(userRepository.findById(any())).willReturn(Optional.empty());
      MessageCreateRequest request = new MessageCreateRequest("Hello", channelId, authorId);

      // when & then
      assertThrows(
          UserNotFoundException.class,
          () -> messageService.create(request, null)
      );
    }

    @Test
    @DisplayName("메시지 생성 실패 - channel not found")
    void create_message_failure_when_channel_not_found() {
      // given
      UUID authorId = UUID.randomUUID();
      User author = new User("홍길동", "hong@codeit.com", "hong1234", null);
      ReflectionTestUtils.setField(author, "id", authorId);

      UUID channelId = UUID.randomUUID();

      given(userRepository.findById(any())).willReturn(Optional.of(author));
      given(channelRepository.findById(any())).willReturn(Optional.empty());

      MessageCreateRequest request = new MessageCreateRequest("Hello", channelId, authorId);

      // when & then
      assertThrows(
          ChannelNotFoundException.class,
          () -> messageService.create(request, null)
      );
    }
  }


  @Nested
  @DisplayName("메시지 수정")
  class UpdateTest {

    @Test
    @DisplayName("메시지 수정 성공")
    void update_message_success() {
      // given
      User author = mock(User.class);
      Channel channel = mock(Channel.class);

      UUID messageId = UUID.randomUUID();
      Message message = new Message("old", channel, author, Collections.emptyList());
      ReflectionTestUtils.setField(message, "id", messageId);

      given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

      MessageDto messageDto = new MessageDto(
          message.getId(),
          Instant.now(),
          Instant.now(),
          "new",
          null,
          null,
          null
      );
      given(messageMapper.toDto(any())).willReturn(messageDto);

      // when
      MessageDto result = messageService.update(messageId, new MessageUpdateRequest("new"));

      // then
      then(messageMapper).should().toDto(message);
      assertThat(result.content()).isEqualTo("new");
    }

    @Test
    @DisplayName("메시지 수정 실패 - message not found")
    void update_message_failure_when_message_not_found() {
      // given
      UUID messageId = UUID.randomUUID();
      given(messageRepository.findById(messageId)).willReturn(Optional.empty());

      // when & then
      assertThrows(
          MessageNotfoundException.class,
          () ->  messageService.update(messageId, new MessageUpdateRequest("content"))
      );
    }
  }


  @Nested
  @DisplayName("메시지  삭제")
  class DeleteTest {

    @Test
    @DisplayName("메시지 삭제 성공")
    void delete_message_success() {
      // given
      UUID messageId = UUID.randomUUID();
      given(messageRepository.existsById(messageId)).willReturn(true);

      // when
      messageService.delete(messageId);

      // then
      then(messageRepository).should().deleteById(messageId);
    }

    @Test
    @DisplayName("메시지 삭제 실패")
    void delete_message_failure_when_message_not_found() {
      // given
      UUID messageId = UUID.randomUUID();
      given(messageRepository.existsById(messageId)).willReturn(false);

      // when & then
      assertThrows(
          MessageNotfoundException.class,
          () -> messageService.delete(messageId)
      );
    }
  }

  @Nested
  @DisplayName("메시지 조회 by channelId")
  class FindAllByChannelIdTest {

    @Test
    @DisplayName("메시지 조회 성공")
    void find_all_messages_in_channel() {
      // given
      UUID messageId = UUID.randomUUID();
      UUID channelId = UUID.randomUUID();

      Message message = new Message("Hello", null, null, Collections.emptyList());
      ReflectionTestUtils.setField(message, "id", UUID.randomUUID());

      MessageDto messageDto = new MessageDto(
          messageId,
          Instant.now(),
          Instant.now(),
          "hello",
          null,
          null,
          null
      );
      Slice<Message> messageSlice = new SliceImpl<>(List.of(message));

      given(channelRepository.existsById(channelId)).willReturn(true);
      given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(), any()))
          .willReturn(messageSlice);
      given(messageMapper.toDto(message)).willReturn(messageDto);

      PageResponse pageResponse = new PageResponse<>(
          List.of(messageDto),
          null,
          1,
          false,
          null
      );
      given(pageResponseMapper.fromSlice(any(), any()))
          .willReturn(pageResponse);

      // when
      PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, null, Pageable.unpaged());

      // then
      then(channelRepository).should().existsById(channelId);
      then(messageRepository).should().findAllByChannelIdWithAuthor(eq(channelId), any(), any());
      then(pageResponseMapper).should().fromSlice(any(), any());
    }
  }
}
