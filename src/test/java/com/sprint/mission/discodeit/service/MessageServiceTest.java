package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.DeleteMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.execption.message.MessageNotFoundException;
import com.sprint.mission.discodeit.global.error.execption.message.NotMessageCreatorException;
import com.sprint.mission.discodeit.global.util.MultipartFileConverter;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileStorage;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MessageServiceTest {

  private MessageRepository messageRepository;
  private BinaryContentRepository binaryContentRepository;
  private UserRepository userRepository;
  private ChannelRepository channelRepository;

  private UserValidator userValidator;
  private ChannelValidator channelValidator;

  private MultipartFileConverter multipartFileConverter;

  private MessageMapper messageMapper;
  private MessageService messageService;
  private FileStorage fileStorage;


  @BeforeEach
  void setUp() {
    jcfSetUp();
//        fileSetUp();
    userValidator = new UserValidator(userRepository);
    channelValidator = new ChannelValidator(channelRepository);
    multipartFileConverter = new MultipartFileConverter();
    messageMapper = new MessageMapper();
    messageService = new BasicMessageService(messageRepository, binaryContentRepository,
        userValidator,
        channelValidator, multipartFileConverter, messageMapper);
  }

  @AfterEach
  void clean() {
    if (fileStorage != null) {
      fileStorage.clearDataDirectory();
    }
  }

  private void jcfSetUp() {
    messageRepository = new JCFMessageRepository();
    binaryContentRepository = new JCFBinaryContentRepository();
    userRepository = new JCFUserRepository();
    channelRepository = new JCFChannelRepository();
  }

  private void fileSetUp() {
    fileStorage = new FileStorage();
    messageRepository = new FileMessageRepository(fileStorage);
    userRepository = new FileUserRepository(fileStorage);
    channelRepository = new FileChannelRepository(fileStorage);
    binaryContentRepository = new JCFBinaryContentRepository(); // 추후 변경
  }

  private User createUser(int num) {
    User user = User.of("test" + num, "nickname" + num, "email" + num, "password" + num);
    return userRepository.saveUser(user);
  }

  private Channel createPublicChannel(String channelName, String description) {
    Channel channel = Channel.of(channelName, description, ChannelType.PUBLIC);
    return channelRepository.saveChannel(channel);
  }

  private Message createMessage(User user, Channel channel, String content) {
    Message message = Message.of(user, channel, content);
    return messageRepository.saveMessage(message);
  }

  @Nested
  @DisplayName("메세지 생성 테스트")
  class createMessageTest {

    @Test
    @DisplayName("메세지 생성 성공")
    void success() {
      // given
      User user = createUser(0);

      String channelName = "channel1";
      String channelDescription = "description";
      Channel channel = createPublicChannel(channelName, channelDescription);

      String content = "hello";
      createMessage(user, channel, content);

      CreateMessageRequest createMessageRequest =
          new CreateMessageRequest(content, channel.getId(), user.getId());

      // when
      MessageDto createdMessage = messageService.createMessage(createMessageRequest,
          new ArrayList<>());

      // then
      assertEquals(content, createdMessage.content());
    }
  }

  @Nested
  @DisplayName("메세지 단일 조회 테스트")
  class findMessageByIdOrThrowTest {

    @Test
    @DisplayName("메세지 단일 조회 성공")
    void success() {
      // given
      User user = createUser(0);

      String channelName = "channel1";
      String channelDescription = "description";
      Channel channel = createPublicChannel(channelName, channelDescription);

      String content = "hello";
      Message message = createMessage(user, channel, content);

      // when
      MessageDto foundMessage = messageService.findMessageByIdOrThrow(message.getId());

      // then
      assertEquals(content, foundMessage.content());
    }

    @Test
    @DisplayName("메세지 아이디 존재 여부 확인 후 에외 발생")
    void findMessageByIdOrThrow_ThrowsException_WhenMessageIdDoesNotExist() {
      UUID randomId = UUID.randomUUID();

      assertThatThrownBy(() -> messageService.findMessageByIdOrThrow(randomId))
          .isInstanceOf(MessageNotFoundException.class)
          .hasMessage(ErrorCode.MESSAGE_NOT_FOUND.format("id: " + randomId));
    }
  }

  @Nested
  @DisplayName("메세지 목록 조회 테스트")
  class findAllMessageTest {

    @Test
    @DisplayName("메세지 목록 조회 성공")
    void success() {
      // given
      User user = createUser(0);

      String channelName = "channel1";
      String channelDescription = "description";
      Channel channel = createPublicChannel(channelName, channelDescription);

      String content1 = "hello";
      String content2 = "world";
      String content3 = "!!";
      Message message1 = createMessage(user, channel, content1);
      Message message2 = createMessage(user, channel, content2);
      Message message3 = createMessage(user, channel, content3);

      // when
      List<MessageDto> foundMessageList = messageService.findAllMessagesByChannelId(
          channel.getId());

      // then
      assertEquals(3, foundMessageList.size());
    }
  }

  @Nested
  @DisplayName("메세지 수정 테스트")
  class updateMessageTest {

    @Test
    @DisplayName("메세지 수정 성공")
    void success() {
      // given
      User user = createUser(0);

      String channelName = "channel1";
      String channelDescription = "description";
      Channel channel = createPublicChannel(channelName, channelDescription);

      String content = "hello";
      Message message = createMessage(user, channel, content);

      String updateContent = "hi";
      UpdateMessageRequest updateMessageRequest =
          new UpdateMessageRequest(updateContent);

      // when
      MessageDto updatedMessage = messageService.updateMessage(message.getId(),
          updateMessageRequest);

      // then
      assertEquals(updateContent, updatedMessage.content());
    }

    //    @Test
    @DisplayName("메세지 생성자 일치 여부 확인 후 예외 발생")
    void updateMessage_ThrowsException_WhenOwnerDoesNotMatch() {
      // given
      User user = createUser(0);
      User anotherUser = createUser(1);

      String channelName = "channel1";
      String channelDescription = "description";
      Channel channel = createPublicChannel(channelName, channelDescription);

      String content = "hello";
      Message message = createMessage(user, channel, content);

      String updateContent = "hi";
      UpdateMessageRequest updateMessageRequest = new UpdateMessageRequest(updateContent);

      // when & then
      assertThatThrownBy(() -> messageService.updateMessage(message.getId(), updateMessageRequest))
          .isInstanceOf(NotMessageCreatorException.class)
          .hasMessage(ErrorCode.NOT_MESSAGE_CREATOR.format("id: " + anotherUser.getId()));
    }
  }

  @Nested
  @DisplayName("메세지 삭제 테스트")
  class deleteMessageTest {

    @Test
    @DisplayName("메세지 삭제 성공")
    void success() {
      // given
      User user = createUser(0);

      String channelName = "channel1";
      String channelDescription = "description";
      Channel channel = createPublicChannel(channelName, channelDescription);

      String content = "hello";
      Message message = createMessage(user, channel, content);

      DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(user.getId());

      // when
      messageService.deleteMessage(message.getId());

      // then
      assertNull(messageRepository.findMessageById(message.getId()));

    }

    //    @Test
    @DisplayName("메세지 생성자 일치 여부 확인 후 예외 발생")
    void deleteMessage_ThrowsException_WhenOwnerDoesNotMatch() {
      // given
      User user = createUser(0);
      User anotherUser = createUser(1);

      String channelName = "channel1";
      String channelDescription = "description";
      Channel channel = createPublicChannel(channelName, channelDescription);

      String content = "hello";
      Message message = createMessage(user, channel, content);

      // when & then
      assertThatThrownBy(() -> messageService.deleteMessage(message.getId()))
          .isInstanceOf(NotMessageCreatorException.class)
          .hasMessage(ErrorCode.NOT_MESSAGE_CREATOR.format("id: " + anotherUser.getId()));
    }
  }

}