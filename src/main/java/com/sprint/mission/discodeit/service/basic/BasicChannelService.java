package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.ChannelConstant.PRIVATE_CHANNEL_CANNOT_BE_UPDATED;
import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final EntityValidator validator;
  private final ChannelRepository channelRepository;

  @Override
  public Channel createPrivateChannel(Channel channel) {
    return channelRepository.save(channel);
   }


  @Override
  public Channel createPublicChannel(Channel channel) {
    // TODO : 검증
    return channelRepository.save(channel);
  }

  @Override
  public void validateUserAccess(Channel channel, String userId) {
    if (channel.getIsPrivate()) {
      channel.getParticipatingUsers().stream().filter(id -> id.equals(userId)).findAny().orElseThrow(
          () -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE)
      );
    }
  }

  @Override
  public Channel getChannelById(String channelId) {
    return validator.findOrThrow(Channel.class, channelId, new ChannelNotFoundException());
  }


  /**
   * 모든 Channel 을 조회,
   * accessibleChannelIds -> private 채널 중,
   *
   * @param userId
   * @return
   */
  @Override
  public List<Channel> findAllChannelsByUserId(String userId, List<ReadStatus> participatingChannels) {
    // 모든 Channel 조회
    List<Channel> allChannels = channelRepository.findAll();

    // userId 를 가지는 모든 read status
    Set<String> accessibleChannelIds = participatingChannels.stream()
            .map(ReadStatus::getChannelId)
                .collect(Collectors.toSet());

    // public 이거나 set 에 포함되어 있거나 (set 에 포함되었다 = 해당 private channel 에 user가 참여 중이다)
    return  allChannels.stream()
        .filter(channel -> !channel.getIsPrivate() ||
            accessibleChannelIds.contains(channel.getUUID())).toList();
  }

  /**
   * {@link ChannelUpdateDto}
   * channelId 로 channel 검색 후 없다면 {@link ChannelNotFoundException}
   * channel 이 private 이라면 {@link InvalidOperationException}
   *
   * @param channelId
   * @param channelName
   * @param maxNumberOfPeople
   * @return
   */
  @Override
  public Channel updateChannel(String channelId, String channelName, int maxNumberOfPeople) {

    Channel channel = validator.findOrThrow(Channel.class, channelId, new ChannelNotFoundException());

    if (channel.getIsPrivate()) {
      throw new InvalidOperationException(PRIVATE_CHANNEL_CANNOT_BE_UPDATED);
    }

    synchronized (channel) {
      channel.updateChannelName(channelName);

      channel.updateMaxNumberOfPeople(maxNumberOfPeople);

      channel.updateUpdatedAt();
    }

    return channelRepository.update(channel);
  }

  @Override
  public void deleteChannel(String channelId) {
    validator.findOrThrow(Channel.class, channelId, new ChannelNotFoundException());

    channelRepository.delete(channelId);
  }

  @Override
  public String generateInviteCode(Channel channel) {
    return null;
  }

}
