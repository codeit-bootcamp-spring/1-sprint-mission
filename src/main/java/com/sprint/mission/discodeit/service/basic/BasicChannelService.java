package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.response.FindChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.error.execption.channel.CannotUpdatePrivateChannelException;
import com.sprint.mission.discodeit.global.util.RandomStringGenerator;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.ReadStatusValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;

  private final ChannelValidator channelValidator;
  private final UserValidator userValidator;
  private final ReadStatusValidator readStatusValidator;

  private final ChannelMapper channelMapper;
  private final RandomStringGenerator randomStringGenerator;

  @Override
  public ChannelDto createPublicChannel(CreatePublicChannelRequest createPublicChannelRequest) {
    Channel channel = channelMapper.toEntity(createPublicChannelRequest.name(),
        createPublicChannelRequest.description(), ChannelType.PUBLIC);

    return channelMapper.toChannelDto(channelRepository.saveChannel(channel));
  }

  @Override
  public ChannelDto createPrivateChannel(CreatePrivateChannelRequest createPrivateChannelRequest) {
    // 요구 사항에 name, description 속성 생략 -> 임의 랜던값 지정
    String name = randomStringGenerator.generateRandomString();
    String description = "description";

    Channel channel = channelMapper.toEntity(name, description, ChannelType.PRIVATE);

    // 유저 초대 및 ReadStatus 생성
    createPrivateChannelRequest.participantIds().forEach(
        userId -> {
          // 이 동작은 readStatusService의 createReadStatus 메서드와 동작이 정확히 일치한다.
          // 하지만 이 동작 하나만을 위해 readStatusService를 추가하는 것이 정말 맞는 것일까?
          User user = userValidator.validateUserExistsByUserId(userId);
          readStatusRepository.saveReadStatus(ReadStatus.of(user, channel, Instant.now()));

          channel.addChannelUser(user);
        }
    );

    return channelMapper.toChannelDto(channelRepository.saveChannel(channel));
  }

  @Override
  public FindChannelResponse findChannelByIdOrThrow(UUID channelId) {
    Channel channel = channelValidator.validateChannelExistsByChannelId(channelId);

    // 가장 최근 메세지의 시간 정보(createdAt)
    // 1. repository(DB)에서 가장 최근 메세지의 시간 정보(createdAt)를 필터링하여 가져오는것 -> 가져오는 데이터 양이 적어진다.(O)
    // -> DB로 생각했을 때 가져오는 데이터 양이 더 적을 때 빠를 것이다.
    // 2. 애플리케이션으로 데이터를 가져와서 필터링 -> 메서드 재사용성 증가
    // -> 불필요한 데이터까지 메모리에 올라가게 된다.
    Message foundMessage = messageRepository.findLastMessage().orElse(null);

    // 채널에 메세지가 하나도 없을 때 시간 정보를 null로 해서 보내도 될까? -> 일단 EPOCH 로 기본값 지정
    Instant lastMessageAt = Instant.EPOCH;
    if (foundMessage != null) {
      lastMessageAt = foundMessage.getCreatedAt();
    }

    List<UUID> channelUsersIdList = new ArrayList<>();

    if (channel.isPrivate()) {
      channelUsersIdList = channel.getChannelUserList().stream()
          .map(user -> user.getId())
          .toList();
    }

    return channelMapper.toFindChannelResponse(channel, lastMessageAt, channelUsersIdList);
  }

  @Override
  public List<FindChannelResponse> findAllChannelsByUserId(UUID userId) {
    // PUBLIC + User가 참가한 PRIVATE 채널
    User user = userValidator.validateUserExistsByUserId(userId);

    List<Channel> channelList = channelRepository.findAccessibleChannels(user);

    return channelList.stream()
        .map(channel -> findChannelByIdOrThrow(channel.getId()))
        .toList();
  }

  @Override
  public ChannelDto updateChannel(UUID channelId, UpdateChannelRequest updateChannelRequest) {
    Channel foundChannel = channelValidator.validateChannelExistsByChannelId(channelId);

    if (foundChannel.isPrivate()) {
      throw new CannotUpdatePrivateChannelException("id: " + foundChannel.getId());
    }

    foundChannel.updateChannelInfo(updateChannelRequest.newName(),
        updateChannelRequest.newDescription());

    return channelMapper.toChannelDto(channelRepository.saveChannel(foundChannel));
  }

  @Override
  public void deleteChannel(UUID channelId) {
    Channel foundChannel = channelValidator.validateChannelExistsByChannelId(channelId);

    // Message 삭제
    messageRepository.findAllMessagesByChannel(foundChannel)
        .forEach(message -> messageRepository.removeMessage(message.getId()));

    // ReadStatus 삭제
    foundChannel.getChannelUserList()
        .forEach(user -> {
          // user 검증
          userValidator.validateUserExistsByUserId(user.getId());
          ReadStatus readStatus = readStatusValidator.validateReadStatusExistsByUserId(
              user.getId());
          readStatusRepository.removeReadStatus(readStatus.getId());
        });

    channelRepository.removeChannel(channelId);
  }

  // inviteUsers, leaveUsers 는 남는 시간에 수정
  @Override
  public Channel inviteUsers(UUID channelId, List<User> invitedUsers) {
    Channel foundChannel = channelValidator.validateChannelExistsByChannelId(channelId);

    invitedUsers.forEach(user -> foundChannel.addChannelUser(user));

    return channelRepository.saveChannel(foundChannel);
  }

  @Override
  public Channel leaveUsers(UUID channelId, List<User> leaveUsers) {
    Channel foundChannel = channelValidator.validateChannelExistsByChannelId(channelId);

    leaveUsers.forEach(user -> foundChannel.removeUserFromChannel(user));

    return channelRepository.saveChannel(foundChannel);
  }


}
