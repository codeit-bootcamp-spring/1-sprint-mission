package com.sprint.mission.unit.channel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.channel.ChannelManagementServiceImpl;
import com.sprint.mission.discodeit.service.channel.ChannelService;
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
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ChannelManagementServiceUnitTest {


  @Mock
  private ChannelService channelService;
  @Mock
  private UserService userService;
  @Mock
  private ReadStatusService readStatusService;
  @Mock
  private MessageService messageService;
  @InjectMocks
  private ChannelManagementServiceImpl channelManagementService;

  private Channel privateChannel;
  private User user1;
  private User user2;

  @BeforeEach
  void setUp() {
    privateChannel = new Channel(ChannelType.PRIVATE, "private", null, null);
    ReflectionTestUtils.setField(privateChannel, "id", UUID.randomUUID());

    user1 = new User("u1", "u1@gmail.com", "pw", null, null);
    user2 = new User("u2", "u2@gmail.com", "pw", null, null);
    ReflectionTestUtils.setField(user1, "id", UUID.randomUUID());
    ReflectionTestUtils.setField(user2, "id", UUID.randomUUID());
  }

  @Nested
  class CreateChannel {

    @Test
    void createPrivateChannel_success() {
      // given
      List<User> users = List.of(user1, user2);

      List<String> userIds = List.of(
          user1.getId().toString(),
          user2.getId().toString()
      );

      given(userService.findAllUsersIn(userIds)).willReturn(users);

      given(channelService.createPrivateChannel(any(Channel.class)))
          .willReturn(privateChannel);

      //when
      Channel result = channelManagementService.createPrivateChannel(privateChannel, userIds);

      //then
      assertThat(result).isEqualTo(privateChannel);
      assertThat(privateChannel.getStatuses()).hasSize(2);

      then(userService).should().findAllUsersIn(userIds);
      then(channelService).should().createPrivateChannel(privateChannel);
    }

    @Test
    void createPrivateChannel_fail_noUserIds() {
      // given
      List<String> userIds = List.of();
      given(userService.findAllUsersIn(any())).willThrow(new IllegalArgumentException());

      // when & then
      assertThatThrownBy(
          () -> channelManagementService.createPrivateChannel(privateChannel, userIds))
          .isInstanceOf(IllegalArgumentException.class);

      then(userService).should().findAllUsersIn(userIds);
      then(channelService).shouldHaveNoInteractions();
    }

    @Test
    void createPublicChannel_success() {
      //given
      Channel publicChannel = new Channel(ChannelType.PUBLIC, "public", "public channel", null);
      given(channelService.createPublicChannel(publicChannel)).willReturn(publicChannel);

      //when
      Channel result = channelManagementService.createPublicChannel(publicChannel);

      //then
      then(channelService).should().createPublicChannel(any(Channel.class));
      assertThat(result).isEqualTo(publicChannel);
    }

    @Test
    void createPublicChannel_fail() {
      //given
      Channel publicChannel = new Channel(ChannelType.PUBLIC, "public", "public channel", null);
      given(channelService.createPublicChannel(any(Channel.class))).willThrow(
          new IllegalArgumentException());

      // when & then
      assertThatThrownBy(() -> channelManagementService.createPublicChannel(publicChannel))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  class LatestMessage {

    @Test
    void getLatestMessageTimeForChannel_returnEpoch_ifNone() {
      // given
      String channelId = UUID.randomUUID().toString();
      given(messageService.getLatestMessageByChannel(any())).willReturn(null);

      //when
      Instant result = channelManagementService.getLastMessageTimeForChannel(channelId);

      // then
      assertThat(result).isEqualTo(Instant.EPOCH);
      then(messageService).should().getLatestMessageByChannel(channelId);
    }

    @Test
    void getLatestMessageTimeForChannel_returnIfExists() {
      //given
      Message message = TestEntityFactory.createMessageWithNoAttachments();
      Channel channel = TestEntityFactory.createPrivateChannel();
      message.addChannel(channel);
      String channelId = message.getChannel().toString();
      Instant createdAt = message.getCreatedAt();
      given(messageService.getLatestMessageByChannel(channelId))
          .willReturn(message);

      //when

      Instant result = channelManagementService.getLastMessageTimeForChannel(channelId);

      //then
      assertThat(result).isEqualTo(createdAt);
    }
  }

  @Nested
  class ChannelParticipants {

    @Test
    void getChannelParticipants_success() {
      Channel channel = TestEntityFactory.createPrivateChannel();
      User user1 = TestEntityFactory.createUser("t1", "t@gmail.com");
      User user2 = TestEntityFactory.createUser("t2", "t2@gmail.com");
      ReadStatus status = new ReadStatus(channel, user1);
      ReadStatus status2 = new ReadStatus(channel, user2);

      given(readStatusService.findAllByChannelId(channel.getId().toString()))
          .willReturn(List.of(status, status2));

      // when
      List<User> result = channelManagementService.getChannelParticipants(
          channel.getId().toString());

      //then
      assertThat(result).hasSize(2);
      assertThat(result).containsExactlyInAnyOrder(user1, user2);
      then(readStatusService).should().findAllByChannelId(channel.getId().toString());
    }

    @Test
    void testFindAllChannelsForUser_flow() {
      //given
      User user = TestEntityFactory.createUser("u1", "u@gmail.com");

      Channel channel1 = TestEntityFactory.createPrivateChannel();
      Channel channel2 = TestEntityFactory.createPrivateChannel();
      Channel channel3 = TestEntityFactory.createPrivateChannel();

      ReadStatus status1 = new ReadStatus(channel1, user);
      ReadStatus status2 = new ReadStatus(channel2, user);
      ReadStatus status3 = new ReadStatus(channel3, user);

      List<ReadStatus> statuses = List.of(status1, status2);
      List<UUID> channelIds = List.of(channel1.getId(), channel2.getId());

      given(readStatusService.findAllByUserId(user.getId().toString()))
          .willReturn(statuses);

      given(channelService.findAllChannelsInOrPublic(channelIds))
          .willReturn(List.of(channel1, channel2, channel3));

      given(readStatusService.findAllInChannel(List.of(channel3.getId())))
          .willReturn(List.of(status3));

      given(userService.findAllUsersIn(List.of(user.getId().toString())))
          .willReturn(List.of(user));

      // when
      List<Channel> result = channelManagementService.findAllChannelsForUser(
          user.getId().toString());

      //then
      assertThat(result).containsExactlyInAnyOrder(channel1, channel2, channel3);
      then(readStatusService).should().findAllByUserId(user.getId().toString());
      then(channelService).should().findAllChannelsInOrPublic(channelIds);
      then(readStatusService).should().findAllInChannel(List.of(channel3.getId()));
      then(userService).should().findAllUsersIn(List.of(user.getId().toString()));
    }

    @Test
    void findAllChannelsForUser_returnsEmpty_whenNoReadStatusesFound() {
      // given
      String userId = user1.getId().toString();
      given(readStatusService.findAllByUserId(userId)).willReturn(List.of());
      given(channelService.findAllChannelsInOrPublic(List.of())).willReturn(List.of());
      given(readStatusService.findAllInChannel(List.of())).willReturn(List.of());
      given(userService.findAllUsersIn(List.of())).willReturn(List.of());

      // when
      List<Channel> result = channelManagementService.findAllChannelsForUser(userId);

      // then
      assertThat(result).isEmpty();
      then(readStatusService).should().findAllByUserId(userId);
      then(channelService).should(times(2)).findAllChannelsInOrPublic(List.of());
      then(readStatusService).should().findAllInChannel(List.of());
      then(userService).should().findAllUsersIn(List.of());
    }

    @Test
    void findAllChannelsForUser_fail_whenUserIdIsInvalid() {
      // given
      String invalidId = "invalid-id";
      given(readStatusService.findAllByUserId(invalidId)).willThrow(IllegalArgumentException.class);

      assertThatThrownBy(() -> channelManagementService.findAllChannelsForUser(invalidId))
          .isInstanceOf(IllegalArgumentException.class);
      then(channelService).shouldHaveNoInteractions();
    }
  }

}
