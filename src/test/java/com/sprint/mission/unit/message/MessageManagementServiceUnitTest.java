package com.sprint.mission.unit.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.message.MessageManagementServiceImpl;
import com.sprint.mission.discodeit.service.message.MessageService;
import com.sprint.mission.discodeit.service.user.UserService;
import com.sprint.mission.unit.TestEntityFactory;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class MessageManagementServiceUnitTest {

  @Mock
  private UserService userService;
  @Mock
  private ChannelService channelService;
  @Mock
  private MessageService messageService;
  @Mock
  private MessageMapper messageMapper;
  @Mock
  private BinaryContentMapper binaryContentMapper;
  @Mock
  private BinaryContentService binaryContentService;

  private Message message;
  private Channel channel;
  private User author;

  @InjectMocks
  private MessageManagementServiceImpl messageManagementService;

  @BeforeEach
  void setUp() {
    author = TestEntityFactory.createUser("u", "u@gmail.com");
    channel = TestEntityFactory.createPrivateChannel();
    message = TestEntityFactory.createMessageWithNoAttachments();
  }

  @Nested
  class CreateMessage {

    @Test
    void createMessage_withoutAttachment_success() {
      //given
      String content = "content";
      String channelId = channel.getId().toString();
      String authorId = author.getId().toString();

      given(channelService.findChannelById(channelId))
          .willReturn(channel);
      given(userService.findUserById(authorId))
          .willReturn(author);
      given(messageMapper.toEntity(content))
          .willReturn(message);
      given(messageService.createMessage(message))
          .willReturn(message);

      // when
      Message result = messageManagementService.createMessage(content, channelId, authorId, null);

      //then
      assertThat(result).isEqualTo(message);
      then(channelService).should().findChannelById(channelId);
      then(userService).should().findUserById(authorId);
      then(messageMapper).should().toEntity(content);
      then(messageService).should().createMessage(message);
    }

    @Test
    void createMessage_shouldFail_onChannelNotFound() {
      //given
      String content = "content";
      String channelId = channel.getId().toString();
      String authorId = author.getId().toString();

      given(channelService.findChannelById(channelId))
          .willThrow(new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

      // when & Then
      assertThatThrownBy(
          () -> messageManagementService.createMessage(content, channelId, authorId, null))
          .isInstanceOf(ChannelNotFoundException.class);
    }

    @Test
    void createMessage_withAttachment_success() {
      //given
      String content = message.getContent();
      String channelId = channel.getId().toString();
      String authorId = author.getId().toString();
      MockMultipartFile file1 = new MockMultipartFile("file1", "file1", "file1",
          "file1".getBytes());
      List<MultipartFile> files = List.of(file1);

      BinaryContent binaryContent = new BinaryContent("file1", 1L, "image/jpeg");
      List<BinaryContent> binaryContents = List.of(binaryContent);

      given(channelService.findChannelById(channelId))
          .willReturn(channel);
      given(userService.findUserById(authorId))
          .willReturn(author);
      given(messageMapper.toEntity(content))
          .willReturn(message);
      given(binaryContentMapper.fromMessageFiles(files))
          .willReturn(binaryContents);
      given(messageService.createMessage(message))
          .willReturn(message);
//    given(message.getAttachments())
//        .willReturn(Collections.emptyList());
      //when
      Message result = messageManagementService.createMessage(content, channelId, authorId, files);

      //then
      assertThat(result).isEqualTo(message);
      assertThat(result.getAttachments()).hasSize(1);
      then(channelService).should().findChannelById(channelId);
      then(userService).should().findUserById(authorId);
      then(binaryContentService).should().saveBinaryContents(binaryContents, files);
      then(messageMapper).should().toEntity(content);
      then(messageService).should().createMessage(message);
    }
  }

  @Nested
  class FindMessage {

    @Test
    void findSingleMessage_shouldCall() {
      // given
      String messageId = message.getId().toString();
      given(messageService.getMessageById(messageId)).willReturn(message);

      // when
      Message result = messageManagementService.findSingleMessage(messageId);

      // then
      assertThat(result).isEqualTo(message);
      then(messageService).should().getMessageById(messageId);
    }

    @Test
    void findSingleMessage_shouldThrow_messageNotFound() {
      //given
      String messageId = UUID.randomUUID().toString();
      given(messageService.getMessageById(messageId))
          .willThrow(new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND));

      //when & then
      assertThatThrownBy(() -> messageManagementService.findSingleMessage(messageId))
          .isInstanceOf(MessageNotFoundException.class);
    }

    @Test
    void findMessagesByChannel_shouldCall() {
      // given
      String channelId = channel.getId().toString();
      Instant cursor = Instant.now();
      Pageable pageable = PageRequest.of(0, 10);
      message.addAuthor(author);
      message.addChannel(channel);

      List<Message> messages = List.of(message);

      Page<Message> page = new PageImpl<>(messages);

      given(messageService.getMessagesByChannelWithCursor(channelId, cursor, pageable))
          .willReturn(page);

      // when
      Page<Message> result = messageManagementService.findMessagesByChannel(channelId, cursor,
          pageable);

      // then
      assertThat(result).isEqualTo(page);
      assertThat(result).containsExactly(message);
      then(messageService).should().getMessagesByChannelWithCursor(channelId, cursor, pageable);
      then(userService).should().findByAllIn(List.of(author.getId()));
    }
  }

  @Nested
  class UpdateMessage {

    @Test
    void updateMessage_shouldCall() {
      String messageId = message.getId().toString();
      String newContent = "newContent";
      given(messageService.getMessageById(messageId)).willReturn(message);
      given(messageService.updateMessage(message, newContent)).willReturn(message);

      //when
      Message result = messageManagementService.updateMessage(messageId, newContent);

      //then
      assertThat(result).isEqualTo(message);
      then(messageService).should().getMessageById(messageId);
      then(messageService).should().updateMessage(message, newContent);
    }

    @Test
    void updateMessage_shouldFail_wrongMessageId() {
      // given
      String messageId = UUID.randomUUID().toString();
      given(messageService.getMessageById(messageId)).willThrow(
          new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND));

      // when & then
      assertThatThrownBy(() -> messageManagementService.updateMessage(messageId, "content"))
          .isInstanceOf(MessageNotFoundException.class);
    }
  }

  @Nested
  class DeleteMessage {

    @Test
    void deleteMessage_shouldCall() {
      String messageId = message.getId().toString();

      //when
      messageManagementService.deleteMessage(messageId);

      // then
      then(messageService).should().deleteMessage(messageId);
    }
  }


}
