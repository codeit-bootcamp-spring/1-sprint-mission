package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.message.MessageContentUpdateRequest;
import com.sprint.mission.discodeit.entity.message.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private PageResponse<MessageDto> mapper;

  @InjectMocks
  private BasicMessageService messageService;


  @Test
  @DisplayName("메세지 생성 성공")
  void sendMessage() throws Exception {
    Channel channel = new Channel("ch1", "channel", ChannelType.PUBLIC);
    User author = new User("user", "user@mail.com", "password1234", null, null);
    setAuthorId(author);
    setChannelId(channel);

    MessageCreateRequest request = new MessageCreateRequest("body", channel.getId(),
        author.getId());
    Message message = new Message("body", channel, author, null);

    when(messageRepository.save(any(Message.class))).thenReturn(message);
    when(channelRepository.findById(any(UUID.class))).thenReturn(Optional.of(channel));
    when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(author));

    MessageDto createMessage = messageService.create(request, null);

    assertNotNull(createMessage);
    assertEquals("body", createMessage.content());
    assertEquals(channel.getId(), createMessage.channelId());
    assertEquals(author.getId(), createMessage.author().id());

    verify(messageRepository).save(any(Message.class));
  }

  @Test
  @DisplayName("메세지 생성 실패 - 없는 채널에서 보내기")
  void failSendMessageWithNotFoundChannel() throws Exception {
    Channel channel = new Channel("ch1", "channel", ChannelType.PUBLIC);
    User author = new User("user", "user@mail.com", "password1234", null, null);
    setAuthorId(author);
    setChannelId(channel);

    MessageCreateRequest request = new MessageCreateRequest("body", channel.getId(),
        author.getId());

    assertThrows(ChannelNotFoundException.class,
        () -> messageService.create(request, null));

    verify(messageRepository, never()).save(any(Message.class));
  }

  @Test
  @DisplayName("메세지 생성 실패 - author가 존재하지 않음")
  void failSendMessageWithNotFound() throws Exception {
    Channel channel = new Channel("ch1", "channel", ChannelType.PUBLIC);
    User author = new User("user", "user@mail.com", "password1234", null, null);
    setAuthorId(author);
    setChannelId(channel);

    MessageCreateRequest request = new MessageCreateRequest("body", channel.getId(),
        author.getId());

    when(channelRepository.findById(any(UUID.class))).thenReturn(Optional.of(channel));

    assertThrows(UserNotFoundException.class,
        () -> messageService.create(request, null));

    verify(messageRepository, never()).save(any(Message.class));
  }


  @Test
  @DisplayName("메세지 삭제 성공")
  void deleteMessage() throws Exception {
    UUID messageId = UUID.randomUUID();

    when(messageRepository.existsById(messageId)).thenReturn(true);

    doNothing().when(messageRepository).deleteById(messageId);

    messageService.remove(messageId);

    verify(messageRepository).existsById(messageId);
    verify(messageRepository).deleteById(messageId);
  }

  @Test
  @DisplayName("메세지 삭제 실패")
  void failDeleteMessage() throws Exception {
    // 존재하지 않는 메세지 삭제 시
    UUID channelId = UUID.randomUUID();

    assertThrows(MessageNotFoundException.class, () -> messageService.remove(channelId));

    verify(messageRepository, never()).deleteById(channelId);
  }

  @Test
  @DisplayName("메세지 수정")
  void modifyMessage() throws Exception {
    MessageContentUpdateRequest request = new MessageContentUpdateRequest("newContent");
    UUID messageId = UUID.randomUUID();
    List<BinaryContent> attachments = new ArrayList<>();
    User user = new User("user", "user@mail.com", "password123", null, null);
    Channel channel = new Channel("ch1", "channel", ChannelType.PUBLIC);
    Message message = new Message("old content", channel, user, attachments);

    when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

    MessageDto updateMessage = messageService.update(messageId, request);

    assertNotNull(updateMessage);
  }

  @Test
  @DisplayName("메세지 수정 - 실패")
  void failModifyMessage() throws Exception {
    // 메세지가 존재하지 않음
    MessageContentUpdateRequest request = new MessageContentUpdateRequest("newContent");
    UUID messageId = UUID.randomUUID();

    when(messageRepository.findById(messageId)).thenThrow(MessageNotFoundException.class);

    assertThrows(MessageNotFoundException.class, () -> messageService.update(messageId, request));
  }

  private void setAuthorId(User author) {
    UUID authorId = UUID.randomUUID();
    Field idField = ReflectionUtils.findField(Message.class, "id");
    ReflectionUtils.makeAccessible(idField);
    ReflectionUtils.setField(idField, author, authorId);
  }

  private void setChannelId(Channel channel) {
    UUID channelId = UUID.randomUUID();
    Field idField = ReflectionUtils.findField(Channel.class, "id");
    ReflectionUtils.makeAccessible(idField);
    ReflectionUtils.setField(idField, channel, channelId);
  }
}