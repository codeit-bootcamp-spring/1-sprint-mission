package com.sprint.mission.discodeit.unitTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

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

  private UUID messageId;
  private UUID channelId;
  private UUID authorId;
  private Message message;
  private MessageDto messageDto;
  private Channel channel;
  private User user;
  private UserDto author;

  @BeforeEach
  void setUp() {
    messageId = UUID.randomUUID();
    channelId = UUID.randomUUID();
    authorId = UUID.randomUUID();
    channel = new Channel(ChannelType.PUBLIC, "Codeit Channel", "Public Codeit Channel");
    user = new User("woody", "woody@naver.com", "1234", null);
    author = new UserDto(authorId, "woody", "woody@naver.com", null, true);
    message = new Message("Hello, This is first message",
        new Channel(ChannelType.PUBLIC, "Codeit Channel", "Public Codeit Channel"),
        new User("woody", "woody@naver.com", "1234", null), List.of());
    messageDto = new MessageDto(messageId, Instant.now(), Instant.now(), "Hello I'm sprinter",
        channelId, author, List.of());
  }

  @Test
  void shouldCreateMessageSuccess() {
    //given
    MessageCreateRequest request = new MessageCreateRequest("Hello I'm sprinter", channelId,
        authorId);
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
    given(userRepository.findById(authorId)).willReturn(Optional.of(user));
    given(messageRepository.save(any())).willReturn(message);
    given(messageMapper.toDto(any())).willReturn(messageDto);

    //when
    MessageDto result = messageService.create(request, List.of());

    //then
    assertThat(result).isNotNull();
    assertThat(result.content()).isEqualTo("Hello I'm sprinter");
    verify(messageRepository, times(1)).save(any());
  }

  @Test
  void shouldFindMessageSuccess() {
    //given
    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    given(messageMapper.toDto(any())).willReturn(messageDto);

    //when
    MessageDto result = messageService.find(messageId);

    //then
    assertThat(result).isNotNull();
    assertThat(result.content()).isEqualTo("Hello I'm sprinter");
  }

  @Test
  void shouldUpdateMessageSuccess() {
    //given
    MessageUpdateRequest request = new MessageUpdateRequest("I'm sprinter");
    given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
    given(messageMapper.toDto(any())).willReturn(messageDto);

    //when
    MessageDto result = messageService.update(messageId, request);

    //then
    assertThat(result).isNotNull();
    verify(messageRepository, times(1)).findById(messageId);
  }

  @Test
  void shouldDeleteMessageSuccess() {
    //given
    given(messageRepository.existsById(messageId)).willReturn(true);

    //when
    messageService.delete(messageId);

    //then
    verify(messageRepository, times(1)).deleteById(messageId);
  }


}