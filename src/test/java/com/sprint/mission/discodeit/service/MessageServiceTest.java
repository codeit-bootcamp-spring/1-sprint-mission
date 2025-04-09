package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private MessageMapper messageMapper;

  @InjectMocks
  private MessageServiceImpl messageService;

  @Captor
  private ArgumentCaptor<Message> messageCaptor;

  private User testUser;
  private Channel testChannel;
  private Message testMessage;
  private MessageDto testMessageDto;
  private UUID userId;
  private UUID channelId;
  private UUID messageId;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    channelId = UUID.randomUUID();
    messageId = UUID.randomUUID();

    testUser = User.builder()
        .id(userId)
        .name("testUser")
        .email("test@example.com")
        .password("password")
        .build();

    testChannel = Channel.builder()
        .id(channelId)
        .name("testChannel")
        .description("test description")
        .type(ChannelType.PUBLIC)
        .build();

    LocalDateTime nowDateTime = LocalDateTime.now();
    Instant nowInstant = Instant.now();
    testMessage = Message.builder()
        .id(messageId)
        .content("Test message content")
        .author(testUser)
        .channel(testChannel)
        .createdAt(nowInstant)
        .updatedAt(nowDateTime)
        .build();

    testMessageDto = MessageDto.builder()
        .id(testMessage.getId())
        .channelId(testMessage.getChannel().getId())
        .authorId(testMessage.getAuthor().getId())
        .author(testMessage.getAuthor().getName())
        .channelName(testMessage.getChannel().getName())
        .content(testMessage.getContent())
        .createdAt(testMessage.getCreatedAt())
        .updatedAt(testMessage.getUpdatedAt())
        .build();
  }

  @Test
  @DisplayName("메시지 생성 성공 테스트")
  void createMessage_Success() {
    // Given
    String newMessageContent = "New message content";
    MessageDto newMessageDto = MessageDto.builder()
        .content(newMessageContent)
        .authorId(userId)
        .channelId(channelId)
        .build();

    UUID savedMessageId = UUID.randomUUID();
    Instant savedCreatedAt = Instant.now();
    Message savedMessage = Message.builder()
        .id(savedMessageId)
        .content(newMessageContent)
        .author(testUser)
        .channel(testChannel)
        .createdAt(savedCreatedAt)
        .build();

    MessageDto expectedDto = MessageDto.builder()
        .id(savedMessageId)
        .content(newMessageContent)
        .authorId(userId)
        .channelId(channelId)
        .createdAt(savedCreatedAt)
        .author(testUser.getName())
        .channelName(testChannel.getName())
        .updatedAt(null)
        .build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
    when(channelRepository.findById(channelId)).thenReturn(Optional.of(testChannel));
    when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);
    when(messageMapper.toDto(savedMessage)).thenReturn(expectedDto);

    // When
    MessageDto createdMessageDto = messageService.createMessage(newMessageDto);

    // Then
    assertThat(createdMessageDto).isNotNull();
    assertThat(createdMessageDto.getContent()).isEqualTo(newMessageContent);
    assertThat(createdMessageDto.getAuthorId()).isEqualTo(userId);
    assertThat(createdMessageDto.getChannelId()).isEqualTo(channelId);
    assertThat(createdMessageDto.getId()).isEqualTo(savedMessageId);
    assertThat(createdMessageDto.getCreatedAt()).isEqualTo(savedCreatedAt);
    assertThat(createdMessageDto).isEqualTo(expectedDto);

    verify(messageRepository).save(messageCaptor.capture());
    Message messageToSave = messageCaptor.getValue();
    assertThat(messageToSave.getContent()).isEqualTo(newMessageContent);
    assertThat(messageToSave.getAuthor()).isEqualTo(testUser);
    assertThat(messageToSave.getChannel()).isEqualTo(testChannel);
    assertThat(messageToSave.getId()).isNull();
    assertThat(messageToSave.getCreatedAt()).isNull();
  }

  @Test
  @DisplayName("채널의 메시지 조회 성공 테스트")
  void getChannelMessages_Success() {
    // Given
    List<Message> messageList = Arrays.asList(testMessage);
    when(messageRepository.findAllByChannelId(channelId)).thenReturn(messageList);
    when(messageMapper.toDto(testMessage)).thenReturn(testMessageDto);

    // When
    List<MessageDto> result = messageService.getChannelMessages(channelId);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(testMessageDto);

  }

  @Test
  @DisplayName("채널의 메시지 조회 실패 테스트 - 빈 목록")
  void getChannelMessages_EmptyList() {
    // Given
    when(messageRepository.findAllByChannelId(channelId)).thenReturn(Collections.emptyList());

    // When
    List<MessageDto> result = messageService.getChannelMessages(channelId);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEmpty();

  }

  @Test
  @DisplayName("메시지 업데이트 성공 테스트")
  void updateMessage_Success() {
    // Given
    String updatedContent = "Updated message content";
    MessageDto updateMessageDto = MessageDto.builder()
        .content(updatedContent)
        .build();

    Message updatedMessage = Message.builder()
        .id(messageId)
        .content(updatedContent)
        .author(testUser)
        .channel(testChannel)
        .createdAt(testMessage.getCreatedAt())
        .updatedAt(testMessage.getUpdatedAt())
        .build();

    MessageDto expectedDto = MessageDto.builder()
        .id(messageId)
        .content(updatedContent)
        .authorId(userId)
        .channelId(channelId)
        .author(testUser.getName())
        .channelName(testChannel.getName())
        .createdAt(testMessage.getCreatedAt())
        .updatedAt(testMessage.getUpdatedAt())
        .build();

    when(messageRepository.findById(messageId)).thenReturn(Optional.of(testMessage));
    when(messageRepository.save(any(Message.class))).thenReturn(updatedMessage);
    when(messageMapper.toDto(updatedMessage)).thenReturn(expectedDto);

    // When
    MessageDto result = messageService.updateMessage(messageId, updateMessageDto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getContent()).isEqualTo(updatedContent);
    assertThat(result).isEqualTo(expectedDto);

    verify(messageRepository).save(messageCaptor.capture());

    Message capturedMessage = messageCaptor.getValue();
    assertThat(capturedMessage.getContent()).isEqualTo(updatedContent);

  }

  @Test
  @DisplayName("메시지 업데이트 실패 테스트 - 메시지 없음")
  void updateMessage_MessageNotFound() {
    // Given
    MessageDto updateMessageDto = MessageDto.builder()
        .content("Updated content")
        .build();

    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> messageService.updateMessage(messageId, updateMessageDto))
        .isInstanceOf(RestApiException.class)
        .hasFieldOrPropertyWithValue("errorCode", DomainErrorCode.MESSAGE_NOT_FOUND);

  }

  @Test
  @DisplayName("채널 ID로 메시지 조회 성공 테스트")
  void findAllByChannelId_Success() {
    // Given
    List<Message> messageList = Arrays.asList(testMessage);
    when(messageRepository.findAllByChannelId(channelId)).thenReturn(messageList);
    when(messageMapper.toDto(testMessage)).thenReturn(testMessageDto);

    // When
    List<MessageDto> result = messageService.findAllByChannelId(channelId);

    // Then
    assertThat(result).isNotNull();
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(testMessageDto);

  }

  @Test
  @DisplayName("메시지 삭제 테스트")
  void deleteMessage_Success() {
    // Given
    when(messageRepository.findById(messageId)).thenReturn(Optional.of(testMessage));
    
    // When
    messageService.deleteMessage(messageId);
    
    // Then
    verify(messageRepository).findById(messageId);
    verify(messageRepository).deleteById(messageId);
  }

  @Test
  @DisplayName("모든 메시지 조회 성공 테스트")
  void findAll_Success() {
    // Given
    List<Message> messageList = Arrays.asList(testMessage);
    when(messageRepository.findAll()).thenReturn(messageList);
    when(messageMapper.toDto(testMessage)).thenReturn(testMessageDto);

    // When
    List<MessageDto> result = messageService.findAll();

    // Then
    assertThat(result).isNotNull();
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(testMessageDto);

  }

  @Test
  @DisplayName("모든 메시지 조회 실패 테스트 - 빈 목록")
  void findAll_EmptyList() {
    // Given
    when(messageRepository.findAll()).thenReturn(Collections.emptyList());

    // When
    List<MessageDto> result = messageService.findAll();

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEmpty();

  }
}
