package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.PageResponse;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.jpa.ChannelRepository;
import com.sprint.mission.discodeit.repository.jpa.MessageRepository;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

  @InjectMocks
  private BasicMessageService basicMessageService;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private MessageMapper messageMapper;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @DisplayName("메시지를 생성할 수 있다.")
  @Test
  void createMessage() {
    //given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageCreateDTO dto = new MessageCreateDTO("Hello", channelId, userId);

    User user = new User("tester", "test@example.com", "password123", null);
    Channel channel = new Channel("channel1", "channel1", ChannelType.PUBLIC);
    Message message = new Message("Hello", user, channel);

    BinaryContentCreateRequest binaryRequest = new BinaryContentCreateRequest("file.png",
        "image/png", new byte[]{1, 2, 3});

    UUID binaryContentId = UUID.randomUUID();
    BinaryContent binaryContent = new BinaryContent("file.png", "image/png", 3L);
    ReflectionTestUtils.setField(binaryContent, "id", binaryContentId);

    MessageDto expectedDto = new MessageDto();

    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(binaryContent);
    given(messageRepository.save(any(Message.class))).willReturn(message);
    given(messageMapper.toDto(any(Message.class))).willReturn(expectedDto);

    //when
    MessageDto result = basicMessageService.create(dto, List.of(binaryRequest));

    //then
    then(userRepository).should().findById(userId);
    then(channelRepository).should().findById(channelId);
    then(binaryContentRepository).should().save(any(BinaryContent.class));
    then(binaryContentStorage).should().put(eq(binaryContentId), any());
    then(messageRepository).should().save(any(Message.class));
    then(messageMapper).should().toDto(any(Message.class));
    assertThat(result).isEqualTo(expectedDto);
  }

  @DisplayName("유저가 존재하지 않은 경우 채널을 생성할 수 없다.")
  @Test
  void createMessage_fail_whenUserNotFound() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageCreateDTO dto = new MessageCreateDTO("hello", channelId, userId);

    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> basicMessageService.create(dto, List.of()))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessageContaining("User을 찾을 수 없습니다.");

    then(userRepository).should().findById(userId);
  }

  @DisplayName("채널이 존재하지 않은 경우 채널을 생성할 수 없다.")
  @Test
  void createMessage_fail_whenChannelNotFound() {
    // given
    UUID userId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageCreateDTO dto = new MessageCreateDTO();
    dto.setAuthorId(userId);
    dto.setChannelId(channelId);
    dto.setContent("Hello");

    User user = new User("user1", "user1@abc.com", "1234", null);
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> basicMessageService.create(dto, List.of()))
        .isInstanceOf(ChannelNotFoundException.class)
        .hasMessageContaining("Channel을 찾을 수 없습니다.");

    then(userRepository).should().findById(userId);
    then(channelRepository).should().findById(channelId);
  }

  @DisplayName("메시지를 업데이트 할 수 있다.")
  @Test
  void updateMessage() {
    // given
    UUID messageId = UUID.randomUUID();
    MessageUpdateDTO dto = new MessageUpdateDTO("Updated content");

    User user = new User("tester", "tester@example.com", "pw", null);
    Channel channel = new Channel("channel", "channel", ChannelType.PUBLIC);
    Message message = new Message("Old content", user, channel);

    MessageDto expectedDto = new MessageDto();
    expectedDto.setContent("Updated content");

    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    given(messageMapper.toDto(message)).willReturn(expectedDto);

    // when
    MessageDto result = basicMessageService.update(messageId, dto);

    // then
    then(messageRepository).should().findById(messageId);
    then(messageMapper).should().toDto(message);
    assertThat(message.getContent()).isEqualTo("Updated content");
    assertThat(result.getContent()).isEqualTo("Updated content");
  }

  @DisplayName("메시지가 없는 경우 메시지 수정이 불가능하다.")
  @Test
  void updateMessage_fail_whenMessageNotFound() {
    // given
    UUID messageId = UUID.randomUUID();
    MessageUpdateDTO dto = new MessageUpdateDTO("Updated content");

    given(messageRepository.findById(messageId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> basicMessageService.update(messageId, dto))
        .isInstanceOf(MessageNotFoundException.class)
        .hasMessageContaining("Message를 찾을 수 없습니다.");

    then(messageRepository).should().findById(messageId);
  }

  @DisplayName("메시지를 삭제할 수 있다.")
  @Test
  void deleteMessage_successfully() {
    // given
    UUID messageId = UUID.randomUUID();
    given(messageRepository.existsById(messageId)).willReturn(true);

    // when
    basicMessageService.delete(messageId);

    // then
    then(messageRepository).should().existsById(messageId);
    then(messageRepository).should().deleteById(messageId);
  }

  @DisplayName("존재하지 않는 메시지 ID로 삭제 시 예외가 발생한다")
  @Test
  void deleteMessage_fail_whenMessageNotFound() {
    // given
    UUID messageId = UUID.randomUUID();
    given(messageRepository.existsById(messageId)).willReturn(false);

    // when & then
    assertThatThrownBy(() -> basicMessageService.delete(messageId))
        .isInstanceOf(MessageNotFoundException.class)
        .hasMessageContaining("Message를 찾을 수 없습니다.");

    then(messageRepository).should().existsById(messageId);
    then(messageRepository).should(never()).deleteById(any());
  }

  @DisplayName("커서 없이 채널 ID로 메시지 목록을 조회하면 첫 페이지 결과와 nextCursor를 반환한다")
  @Test
  void findAllByChannelId_noCursor() {
    //given
    UUID channelId = UUID.randomUUID();
    Pageable pageable = PageRequest.of(0, 2);
    Instant now = Instant.now();

    User user = new User("tester", "email", "pw", null);
    Channel channel = new Channel("channel", "desc", ChannelType.PUBLIC);

    Message msg1 = new Message("Hello1", user, channel);
    Message msg2 = new Message("Hello2", user, channel);

    ReflectionTestUtils.setField(msg1, "createdAt", now.minusSeconds(30));
    ReflectionTestUtils.setField(msg2, "createdAt", now.minusSeconds(20));

    List<Message> messages = List.of(msg1, msg2);
    Page<Message> messagePage = new PageImpl<>(messages, pageable, 10);

    MessageDto dto1 = new MessageDto();
    dto1.setContent("Hello1");

    MessageDto dto2 = new MessageDto();
    dto2.setContent("Hello2");

    given(messageRepository.findAllByChannel_Id(channelId, pageable)).willReturn(messagePage);
    given(messageMapper.toDto(msg1)).willReturn(dto1);
    given(messageMapper.toDto(msg2)).willReturn(dto2);

    //when
    PageResponse<MessageDto> result = basicMessageService.findAllByChannelId(channelId, null,
        pageable);

    //then
    then(messageRepository).should().findAllByChannel_Id(channelId, pageable);
    then(messageMapper).should(times(2)).toDto(any());

    assertThat(result.getContent()).hasSize(2);
    assertThat(result.isHasNext()).isTrue(); // total=10이므로 hasNext=true
    assertThat(result.getNextCursor()).isEqualTo(msg2.getCreatedAt());

  }

  @DisplayName("커서 이후의 메시지를 조회하면 해당 커서 이전 메시지를 반환하고 nextCursor를 설정한다")
  @Test
  void findAllByChannelId_withCursor() {
    // given
    UUID channelId = UUID.randomUUID();
    Instant cursor = Instant.now().minusSeconds(60);
    Pageable pageable = PageRequest.of(0, 2);

    User user = new User("tester", "email", "pw", null);
    Channel channel = new Channel("channel", "desc", ChannelType.PUBLIC);

    Message msg1 = new Message("Hello1", user, channel);
    ReflectionTestUtils.setField(msg1, "createdAt", cursor.minusSeconds(10));

    List<Message> messages = List.of(msg1);
    Page<Message> messagePage = new PageImpl<>(messages, pageable, 5);

    MessageDto dto1 = new MessageDto();
    dto1.setContent("Hello1");

    given(messageRepository.findAllByChannel_IdAndCreatedAtBefore(channelId, cursor, pageable))
        .willReturn(messagePage);
    given(messageMapper.toDto(msg1)).willReturn(dto1);

    // when
    PageResponse<MessageDto> result = basicMessageService.findAllByChannelId(channelId, cursor,
        pageable);

    // then
    then(messageRepository).should()
        .findAllByChannel_IdAndCreatedAtBefore(channelId, cursor, pageable);
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getNextCursor()).isEqualTo(msg1.getCreatedAt());
  }
}