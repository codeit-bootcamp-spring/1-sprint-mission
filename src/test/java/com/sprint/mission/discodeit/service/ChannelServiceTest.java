package com.sprint.mission.discodeit.service;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDTo;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private ChannelMapper channelMapper;
  @Mock
  private MessageMapper messageMapper;
  @Mock
  private UserMapper userMapper;
  @InjectMocks
  private BasicChannelService channelService;

  @Nested
  class createPublicChannel {

    @Test
    void createPublicChannelSuccess() {
      //given
      CreatePublicChannelDto createPublicChannelDto = new CreatePublicChannelDto(
          "public test Channel",
          "테스트용 공개 채널 입니다."
      );

      Channel channel = new Channel(
          "public test Channel",
          ChannelType.PUBLIC,
          "테스트용 공개 채널 입니다."
      );

      UUID channelId = UUID.randomUUID();

      // 리플렉션을 사용하여 id 필드 설정
      try {
        Field idField = BaseEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(channel, channelId);
      } catch (Exception e) {
        fail("Failed to set ID using reflection: " + e.getMessage());
      }

      ChannelDto channelDto = new ChannelDto(
          channelId,
          "public test Channel",
          ChannelType.PUBLIC,
          "테스트용 공개 채널 입니다.",
          channel.getCreatedAt(),
          null,
          null
      );

      when(channelRepository.save(any(Channel.class))).thenReturn(channel);
      when(channelMapper.toDto(any(Channel.class))).thenReturn(channelDto);

      //when
      ChannelDto createdChannelDto = channelService.create(createPublicChannelDto);

      //then
      Assertions.assertAll(
          () -> Assertions.assertNotNull(createdChannelDto),
          () -> Assertions.assertEquals(createPublicChannelDto.name(), createdChannelDto.name()),
          () -> Assertions.assertEquals(ChannelType.PUBLIC, createdChannelDto.type()),
          () -> Assertions.assertEquals(createPublicChannelDto.description(),
              createdChannelDto.description())
      );

      verify(channelRepository, times(1)).save(any(Channel.class));
      verify(channelMapper, times(1)).toDto(channel);
    }


    @Test
    void createPublicChannelFailure() {

    }

  }


  @Nested
  class createPrivateChannel {

    @Test
    void createPrivateChannelSuccess() {
      //given

      User user1 = new User();
      User user2 = new User();

      UUID userId1 = UUID.randomUUID();
      UUID userId2 = UUID.randomUUID();

      // 리플렉션을 사용하여 id 필드 설정
      try {
        Field idField = BaseEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user1, userId1);
        idField.set(user2, userId2);
      } catch (Exception e) {
        fail("Failed to set ID using reflection: " + e.getMessage());
      }

      List<User> participants = new ArrayList<>();
      participants.add(user1);
      participants.add(user2);
      List<String> participantsIds = participants.stream().map(user -> user.getId().toString())
          .toList();

      CreatePrivateChannelDTo createPrivateChannelDTo = new CreatePrivateChannelDTo(
          participantsIds);

      Channel channel = new Channel(
          null,
          ChannelType.PRIVATE,
          null
      );

      UUID channelId = UUID.randomUUID();

      // 리플렉션을 사용하여 id 필드 설정
      try {
        Field idField = BaseEntity.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(channel, channelId);
      } catch (Exception e) {
        fail("Failed to set ID using reflection: " + e.getMessage());
      }

      ChannelDto channelDto = new ChannelDto(
          channelId,
          null,
          ChannelType.PRIVATE,
          null,
          channel.getCreatedAt(),
          null,
          null
      );

      ReadStatus readStatus1 = new ReadStatus(
          channel,
          user1,
          Instant.now(),
          true
      );
      ReadStatus readStatus2 = new ReadStatus(
          channel,
          user2,
          Instant.now(),
          true
      );
      when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
      when(userRepository.findById(userId2)).thenReturn(Optional.of(user2));

      when(readStatusRepository.save(any(ReadStatus.class))).thenReturn(readStatus1);

      when(channelRepository.save(any(Channel.class))).thenReturn(channel);
      when(channelMapper.toDto(any(Channel.class))).thenReturn(channelDto);

      //when
      ChannelDto createdChannelDto = channelService.create(createPrivateChannelDTo);

      //then
      Assertions.assertAll(
          () -> Assertions.assertNotNull(createdChannelDto),
          () -> Assertions.assertNull(createdChannelDto.name()),
          () -> Assertions.assertEquals(ChannelType.PRIVATE, createdChannelDto.type()),
          () -> Assertions.assertNull(null, createdChannelDto.description())
      );

      verify(channelRepository, times(1)).save(any(Channel.class));
      verify(channelMapper, times(1)).toDto(channel);

    }

    @Test
    void createPrivateChannelFailure() {

    }
  }

  @Nested
  class updateChannel {

    @Test
    void updatePublicChannelSuccess() {

    }

    @Test
    void updatePublicChannelFailure() {
    }

    @Test
    void updatePrivateChannelFailure() {
    }

  }

  @Nested
  class deleteChannel {

    @Test
    void deletePublicChannelSuccess() {
    }

    @Test
    void deletePublicChannelFailure() {

    }

    @Test
    void deletePrivateChannelSuccess() {

    }

    @Test
    void deletePrivateChannelFailure() {

    }
  }

  @Nested
  class getChannel {

    @Test
    void getChannelsByUserIdSuccess() {
    }

    @Test
    void getChannelsByUserIdFailure() {

    }
  }

}
