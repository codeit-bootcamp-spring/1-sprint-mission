package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

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

  @InjectMocks
  private BasicMessageService messageService;

  // 메시지 생성 성공 테스트
  @Test
  void testCreateMessage_Success() {
    // given - 채널과 작성자가 존재하고, 첨부파일이 포함된 메시지 생성 요청을 준비함
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("Hello", channelId, authorId);
    BinaryContentCreateRequest binaryReq = new BinaryContentCreateRequest("file.txt", "text/plain",
        new byte[10]);

    Channel channel = new Channel(ChannelType.PUBLIC, "test", "desc");
    User user = new User("user", "email@test.com", "pw", null);

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.of(user));

    Message savedMessage = new Message("Hello", channel, user, List.of());
    ReflectionTestUtils.setField(savedMessage, "id", UUID.randomUUID());
    given(messageRepository.save(any(Message.class))).willReturn(savedMessage);

    // messageMapper.toDto 호출 시 실제 저장된 메시지 객체와 무관하게 일치하는 결과 반환되도록 수정
    MessageDto expectedDto = mock(MessageDto.class);
    given(messageMapper.toDto(any(Message.class))).willReturn(expectedDto);

    // when - 메시지 생성 요청 실행
    MessageDto result = messageService.create(request, List.of(binaryReq));

    // then - 결과 및 의존성 호출 검증
    assertEquals(expectedDto, result);
    verify(channelRepository).findById(channelId);
    verify(userRepository).findById(authorId);
    verify(messageRepository).save(any(Message.class));
    verify(messageMapper).toDto(any(Message.class));
  }

  // 메시지 생성 실패 테스트 - 채널이 존재하지 않을 때
  @Test
  void testCreateMessage_Failure_ChannelNotFound() {
    // given - 존재하지 않는 채널 ID로 요청
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("Hello", channelId, authorId);

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when & then - 예외 발생 검증 및 다른 리포지토리 호출 방지 확인
    assertThrows(ChannelNotFoundException.class, () -> messageService.create(request, List.of()));
    verify(channelRepository).findById(channelId);
    verify(userRepository, never()).findById(any());
    verify(messageRepository, never()).save(any());
  }

  // 메시지 수정 성공 테스트
  @Test
  void testUpdateMessage_Success() {
    // given - 기존 메시지를 수정할 요청 준비
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("Updated content");

    Channel dummyChannel = new Channel(ChannelType.PUBLIC, "channel", "desc");
    User dummyUser = new User("tester", "test@email.com", "pass", null);
    Message message = new Message("Original content", dummyChannel, dummyUser, List.of());
    ReflectionTestUtils.setField(message, "id", messageId);

    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    MessageDto expectedDto = mock(MessageDto.class);
    given(messageMapper.toDto(message)).willReturn(expectedDto);

    // when - 수정 요청 실행
    MessageDto result = messageService.update(messageId, request);

    // then - 수정된 내용 확인 및 의존성 호출 검증
    assertEquals(expectedDto, result);
    verify(messageRepository).findById(messageId);
    verify(messageMapper).toDto(message);
  }

  // 메시지 수정 실패 테스트 - 메시지 없음
  @Test
  void testUpdateMessage_Failure_NotFound() {
    // given - 존재하지 않는 메시지 ID로 요청
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("Updated");

    given(messageRepository.findById(messageId)).willReturn(Optional.empty());

    // when & then - 예외 발생 확인 및 Mapper 호출 방지 확인
    assertThrows(MessageNotFoundException.class, () -> messageService.update(messageId, request));
    verify(messageRepository).findById(messageId);
    verify(messageMapper, never()).toDto(any());
  }

  // 메시지 삭제 성공 테스트
  @Test
  void testDeleteMessage_Success() {
    // given - 존재하는 메시지 ID로 삭제 요청
    UUID messageId = UUID.randomUUID();
    given(messageRepository.existsById(messageId)).willReturn(true);

    // when - 삭제 요청 실행
    messageService.delete(messageId);

    // then - deleteById 호출 검증
    verify(messageRepository).deleteById(messageId);
  }

  // 메시지 삭제 실패 테스트 - 존재하지 않는 메시지
  @Test
  void testDeleteMessage_Failure_NotFound() {
    // given - 존재하지 않는 메시지 ID
    UUID messageId = UUID.randomUUID();
    given(messageRepository.existsById(messageId)).willReturn(false);

    // when & then - 예외 발생 확인 및 deleteById 호출되지 않음 확인
    assertThrows(MessageNotFoundException.class, () -> messageService.delete(messageId));

    verify(messageRepository, never()).deleteById(messageId);
  }
}