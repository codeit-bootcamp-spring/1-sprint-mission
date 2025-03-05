package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.response.FindChannelResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.execption.channel.CannotUpdatePrivateChannelException;
import com.sprint.mission.discodeit.global.error.execption.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.error.execption.channel.NotChannelCreatorException;
import com.sprint.mission.discodeit.global.util.RandomStringGenerator;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileStorage;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.ReadStatusValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

  private ChannelRepository channelRepository;
  private ReadStatusRepository readStatusRepository;
  private MessageRepository messageRepository;
  private UserRepository userRepository;
  private ChannelValidator channelValidator;
  private UserValidator userValidator;
  private ReadStatusValidator readStatusValidator;
  private ChannelMapper channelMapper;
  @Mock
  private RandomStringGenerator randomStringGenerator;
  private ChannelService channelService;
  private FileStorage fileStorage;

  @BeforeEach
  void setUp() {
//        fileSetup();
    jcfSetUp();
    channelValidator = new ChannelValidator(channelRepository);
    userValidator = new UserValidator(userRepository);
    readStatusValidator = new ReadStatusValidator(readStatusRepository);
    channelMapper = new ChannelMapper();
    channelService = new BasicChannelService(channelRepository, readStatusRepository,
        messageRepository,
        channelValidator, userValidator, readStatusValidator, channelMapper, randomStringGenerator);
  }

  @AfterEach
  void clean() {
    if (fileStorage != null) {
      fileStorage.clearDataDirectory();
    }
  }

  private void jcfSetUp() {
    channelRepository = new JCFChannelRepository();
    readStatusRepository = new JCFReadStatusRepository();
    messageRepository = new JCFMessageRepository();
    userRepository = new JCFUserRepository();
  }

  private void fileSetup() {
    fileStorage = new FileStorage();
    userRepository = new FileUserRepository(fileStorage);
    channelRepository = new FileChannelRepository(fileStorage);
    readStatusRepository = new JCFReadStatusRepository(); // 추후 변경
    messageRepository = new JCFMessageRepository(); // 추후 변
  }

  private User createUser(int num) {
    User user = User.of("test" + num, "nickname" + num, "email" + num, "password" + num);
    return userRepository.saveUser(user);
  }

  private ChannelDto createPublicChannel(String channelName, String description) {
    CreatePublicChannelRequest createPublicChannelRequest =
        new CreatePublicChannelRequest(channelName, description);

    return channelService.createPublicChannel(createPublicChannelRequest);
  }

  private ChannelDto createPrivateChannel() {
    int size = 5;
    List<UUID> channelUsersIdList = new ArrayList<>();
    for (int i = 1; i < size; i++) {
      User user = createUser(i);
      channelUsersIdList.add(user.getId());
    }

    CreatePrivateChannelRequest createPrivateChannelRequest =
        new CreatePrivateChannelRequest(channelUsersIdList);

    when(randomStringGenerator.generateRandomString()).thenReturn("test");

    return channelService.createPrivateChannel(createPrivateChannelRequest);
  }

  @Nested
  @DisplayName("채널 생성 테스트")
  class CreateChannelTest {

    @Test
    @DisplayName("Public 채널 생성 성공")
    void successWithPublic() {
      // given
      String channelName = "channel1";
      String description = "description";
      CreatePublicChannelRequest createPublicChannelRequest =
          new CreatePublicChannelRequest(channelName, description);

      // when
      ChannelDto channel = channelService.createPublicChannel(createPublicChannelRequest);

      // then
      assertEquals(channelName, channel.name());
      assertEquals(description, channel.description());

      assertNotNull(channelRepository.findChannelById(channel.id()));
    }

    @Test
    @DisplayName("Private 채널 생성 성공")
    void successWithPrivate() {
      // given
      int size = 5;
      List<User> channelUserList = new ArrayList<>();
      List<UUID> channelUsersIdList = new ArrayList<>();
      for (int i = 1; i < size; i++) {
        User user = createUser(i);
        channelUserList.add(user);
        channelUsersIdList.add(user.getId());
      }

      CreatePrivateChannelRequest createPrivateChannelRequest =
          new CreatePrivateChannelRequest(channelUsersIdList);

      when(randomStringGenerator.generateRandomString()).thenReturn("test");

      // when
      ChannelDto channel = channelService.createPrivateChannel(createPrivateChannelRequest);

      // then
      assertEquals("test", channel.name());
      assertEquals("description", channel.description());

      channelUserList.forEach(user ->
          assertNotNull(readStatusRepository.findReadStatusByUserId(user.getId())));

      assertNotNull(channelRepository.findChannelById(channel.id()));
    }
  }

  @Nested
  @DisplayName("채널 단일 조회 테스트")
  class FindChannelByIdOrThrowTest {

    @Test
    @DisplayName("Public 채널 단일 조회 성공")
    void successWithPublic() {
      // given
      ChannelDto channel = createPublicChannel("channel1", "description");

      // when
      FindChannelResponse findChannelResponse =
          channelService.findChannelByIdOrThrow(channel.id());
      // then
      assertEquals("channel1", findChannelResponse.name());
      assertEquals("description", findChannelResponse.description());
    }

    @Test
    @DisplayName("Private 채널 단일 조회 성공")
    void successWithPrivate() {
      // given
      ChannelDto channel = createPrivateChannel();

      // when
      FindChannelResponse findChannelResponse =
          channelService.findChannelByIdOrThrow(channel.id());

      // then
      assertEquals("test", findChannelResponse.name());
      assertEquals("description", findChannelResponse.description());
    }

    @Test
    @DisplayName("존재하지 않는 채널 아이디 조회 후 예외 발생")
    void findChannelByIdOrThrow_ThrowsException_WhenChannelIdDoesNotExist() {
      UUID randomId = UUID.randomUUID();
      assertThatThrownBy(() -> channelService.findChannelByIdOrThrow(randomId))
          .isInstanceOf(ChannelNotFoundException.class)
          .hasMessage(ErrorCode.CHANNEL_NOT_FOUND.format("id: " + randomId));
    }
  }

  @Nested
  @DisplayName("채널 목록 조회 테스트")
  class FindAllChannelsTest {

    @Test
    @DisplayName("채널 목록 조회 성공")
    void success() {
      // given
      User channelOwner = createUser(0);
      createPublicChannel("channel1", "description");
      createPrivateChannel();

      // when
      List<FindChannelResponse> findChannelResponseList =
          channelService.findAllChannelsByUserId(channelOwner.getId());

      // then
      // channelOwner는 Private channel에 참가하지 않았기에 2개 중 1개만 포함된다.
      assertEquals(1, findChannelResponseList.size());
    }

//    @Test
//    @DisplayName("채널 목록 조회 성공(Private 조건 포함)")
//    void successWithPrivate() {
//      // given
//      User channelOwner1 = createUser(1);
//      createPublicChannel("channel1", "description");
//      createPrivateChannel();
//
//      // when
//      List<FindChannelResponse> findChannelResponseList =
//          channelService.findAllChannelsByUserId(channelOwner1.getId());
//
//      // then
//      // channelOwner1는 Private channel에 참가하였기에 2개 모두 포함한다.
//      assertEquals(2, findChannelResponseList.size());
//    }
  }

  @Nested
  @DisplayName("채널 수정 테스트")
  class UpdateChannelNameTest {

    @Test
    @DisplayName("채널 수정 성공")
    void success() {
      // given
      User channelOwner = createUser(0);
      ChannelDto channel = createPublicChannel("channel1", "description1");

      String updatedName = "channel2";
      String updateDescription = "description2";

      UpdateChannelRequest updateChannelRequest =
          new UpdateChannelRequest(updatedName, updateDescription);

      // when
      ChannelDto updateChannel = channelService.updateChannel(channel.id(), updateChannelRequest);

      // then
      assertEquals(updatedName, updateChannel.name());
      assertEquals(updateDescription, updateChannel.description());
    }

    //    @Test
    @DisplayName("채널 생성자 일치 여부 확인 후 예외 발생")
    void updateChannel_ThrowsException_WhenOwnerDoesNotMatch() {
      // given
      User anotherUser = createUser(1);
      ChannelDto channel = createPublicChannel("channel1", "description1");

      String updatedName = "channel2";
      String updateDescription = "description2";

      UpdateChannelRequest updateChannelRequest =
          new UpdateChannelRequest(updatedName, updateDescription);

      // when & then
      assertThatThrownBy(() -> channelService.updateChannel(channel.id(), updateChannelRequest))
          .isInstanceOf(NotChannelCreatorException.class)
          .hasMessage(ErrorCode.NOT_CHANNEL_CREATOR.format("id: " + anotherUser.getId()));
    }

    @Test
    @DisplayName("Private 여부 확인 후 예외 발생")
    void updateChannel_ThrowsException_WhenStatusIsPrivate() {
      // given
      User anotherUser = createUser(1);
      ChannelDto channel = createPrivateChannel();

      String updatedName = "channel2";
      String updateDescription = "description2";

      UpdateChannelRequest updateChannelRequest =
          new UpdateChannelRequest(updatedName, updateDescription);

      // when & then
      assertThatThrownBy(() -> channelService.updateChannel(channel.id(), updateChannelRequest))
          .isInstanceOf(CannotUpdatePrivateChannelException.class)
          .hasMessage(ErrorCode.CANNOT_UPDATE_PRIVATE_CHANNEL.format("id: " + channel.id()));
    }
  }

  @Nested
  @DisplayName("채널 삭제 테스트")
  class DeleteChannelTest {

    @Test
    @DisplayName("채널 삭제 성공")
    void success() {
      // given
      ChannelDto channel = createPrivateChannel();

      // when
      channelService.deleteChannel(channel.id());

      // then
      assertNull(channelRepository.findChannelById(channel.id()));
//      assertEquals(0, messageRepository.findAllMessagesByChannel(channel).size());
//      channel.getChannelUserList().forEach(
//          user -> assertNull(readStatusRepository.findReadStatusByUserId(user.getId()).orElse(null))
//      );
    }

    //    @Test
    @DisplayName("채널 생성자 일치 여부 확인 후 예외 발생")
    void deleteChannel_ThrowsException_WhenOwnerDoesNotMatch() {
      // given
      User anotherUser = createUser(1);
      ChannelDto channel = createPrivateChannel();

      // when & then
      assertThatThrownBy(() -> channelService.deleteChannel(channel.id()))
          .isInstanceOf(NotChannelCreatorException.class)
          .hasMessage(ErrorCode.NOT_CHANNEL_CREATOR.format("id: " + anotherUser.getId()));
    }
  }
//
//    @Nested
//    @DisplayName("채널 인원 초대 테스트")
//    class inviteUsersTest {
//        @Test
//        @DisplayName("채널 인원 초대 성공")
//        void success() {
//            // given
//            User channelOwner = User.of("test1", "nickname1", "email1",
//                    "password1", "profileImageUrl1", true);
//            User user2 = User.of("test2", "nickname2", "email2",
//                    "password2", "profileImageUrl2", true);
//            User user3 = User.of("test3", "nickname3", "email3",
//                    "password3", "profileImageUrl3", true);
//            User user4 = User.of("test4", "nickname4", "email4",
//                    "password4", "profileImageUrl4", true);
//            String channelName1 = "channel1";
//            Channel channel1 = Channel.of(channelName1, channelOwner);
//
//            List<User> invitedUserList = new ArrayList<>();
//            invitedUserList.add(user2);
//            invitedUserList.add(user3);
//            invitedUserList.add(user4);
//
//            when(channelRepository.findChannelById(channel1.getId())).thenReturn(channel1);
//            when(channelRepository.saveChannel(channel1)).thenReturn(channel1);
//
//            // when
//            Channel invitedChannel = channelService.inviteUsers(channel1.getId(), invitedUserList);
//
//            // then
//            assertArrayEquals(invitedUserList.toArray(), invitedChannel.getChannelUserList().toArray());
//        }
//    }
//
//    @Nested
//    @DisplayName("채널 인원 나가기 테스트")
//    class leaveUsersTest {
//        @Test
//        @DisplayName("채널 인원 나가기 성공")
//        void success() {
//            // given
//            User channelOwner = User.of("test1", "nickname1", "email1",
//                    "password1", "profileImageUrl1", true);
//            User user2 = User.of("test2", "nickname2", "email2",
//                    "password2", "profileImageUrl2", true);
//            User user3 = User.of("test3", "nickname3", "email3",
//                    "password3", "profileImageUrl3", true);
//            User user4 = User.of("test4", "nickname4", "email4",
//                    "password4", "profileImageUrl4", true);
//            String channelName1 = "channel1";
//            Channel channel1 = Channel.of(channelName1, channelOwner);
//            channel1.getChannelUserList().add(user2);
//            channel1.getChannelUserList().add(user3);
//            channel1.getChannelUserList().add(user4);
//
//            List<User> leavedUserList = new ArrayList<>();
//            leavedUserList.add(user2);
//            leavedUserList.add(user3);
//            leavedUserList.add(user4);
//
//            when(channelRepository.findChannelById(channel1.getId())).thenReturn(channel1);
//            when(channelRepository.saveChannel(channel1)).thenReturn(channel1);
//
//            // when
//            Channel leavedChannel = channelService.leaveUsers(channel1.getId(), leavedUserList);
//
//            // then
//            assertEquals(0, leavedChannel.getChannelUserList().size());
//        }
//    }

}