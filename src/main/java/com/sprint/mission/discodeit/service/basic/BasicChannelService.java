package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Channel createPrivateChannel(Channel channel) {
    return channelRepository.save(channel);
  }


  @Override
  @Transactional
  public Channel createPublicChannel(Channel channel) {
    return channelRepository.save(channel);
  }


  @Override
  public void validateUserAccess(Channel channel, User user) {
    log.debug("[VALIDATING USER ACCESS] : [CHANNEL_ID : {}] , [USER_ID : {}]", channel.getId(),
        user.getId());
    if (Objects.equals(channel.getType(), Channel.ChannelType.PRIVATE)) {
      Optional<ReadStatus> status = readStatusRepository.findByUserAndChannel(user, channel);
      if (status.isEmpty()) {
        log.warn("[ATTEMPT TO ACCESS UNAUTHORIZED CHANNEL] : [CHANNEL_ID : {}] , [USER_ID : {}]",
            channel.getId(), user.getId());
        throw new ChannelException(ErrorCode.NO_ACCESS_TO_CHANNEL);

      }
    }
  }

  @Override
  public Channel findChannelById(String channelId) {
    return channelRepository.findById(UUID.fromString(channelId))
        .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
  }

  @Override
  public List<Channel> findAllChannelsInOrPublic(List<UUID> ids) {
    List<Channel> channels = channelRepository.findByIdInOrType(ids, Channel.ChannelType.PUBLIC);
    return channels;
  }

  @Override
  public List<Channel> findByType(Channel.ChannelType type) {
    return channelRepository.findAllByType(type);
  }

//  @Override
//  @Transactional
//  public List<Channel> findAllChannelsByUserId(String userId) {
//    List<Channel> privateChannel = channelRepository.findPrivateChannels(UUID.fromString(userId));
//    return privateChannel;
//  }

  @Override
  @Transactional
  public Channel updateChannel(String channelId, ChannelUpdateDto dto) {

    Channel channel = channelRepository.findById(UUID.fromString(channelId)).orElseThrow(
        () -> {
          log.warn("[FAILED TO FIND CHANNEL] : [ID: {}]", channelId);
          return new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
              Map.of("channelId", channelId));
        }
    );

    log.debug("[FOUND CHANNEL] [ID: {}]", channelId);

    if (Objects.equals(channel.getType(), Channel.ChannelType.PRIVATE)) {
      log.warn("[ATTEMPT TO UPDATE PRIVATE CHANNEL]: [ID: {}]", channelId);
      throw new PrivateChannelUpdateException(ErrorCode.PRIVATE_CHANNEL_CANNOT_BE_UPDATED,
          Map.of("channelId", channelId));

    }

    channel.updateChannelName(dto.newName());
    channel.updateDescription(dto.newDescription());

    return channelRepository.save(channel);

  }

  @Override
  @Transactional
  public void deleteChannel(String channelId) {
    channelRepository.deleteById(UUID.fromString(channelId));
  }

}
