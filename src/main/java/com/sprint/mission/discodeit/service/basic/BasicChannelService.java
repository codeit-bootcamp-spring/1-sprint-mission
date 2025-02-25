package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    return channelRepository.save(channel);
  }


  @Override
  public void validateUserAccess(Channel channel, String userId) {
    if (Objects.equals(channel.getChannelType(), Channel.ChannelType.PRIVATE)) {
      channel.getParticipatingUsers().stream().filter(id -> id.equals(userId)).findAny().orElseThrow(
          () -> new CustomException(ErrorCode.DEFAULT_ERROR_MESSAGE)
      );
    }
  }

  @Override
  public Channel getChannelById(String channelId) {
    return validator.findOrThrow(Channel.class, channelId, new CustomException(ErrorCode.CHANNEL_NOT_FOUND));
  }


  @Override
  public List<Channel> findAllChannelsByUserId(String userId) {

    List<Channel> allChannels = channelRepository.findAll();

    return allChannels.stream()
        .filter(channel -> !Objects.equals(channel.getChannelType(), Channel.ChannelType.PRIVATE)
            || (Objects.equals(channel.getChannelType(), Channel.ChannelType.PRIVATE)
                && channel.getParticipatingUsers().contains(userId)))
        .toList();

  }

  @Override
  public Channel updateChannel(String channelId, ChannelUpdateDto dto) {

    Channel channel = validator.findOrThrow(Channel.class, channelId, new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

    if (Objects.equals(channel.getChannelType(), Channel.ChannelType.PRIVATE)) {
      throw new CustomException(ErrorCode.PRIVATE_CHANNEL_CANNOT_BE_UPDATED);
    }

    channel.updateChannelName(dto.newName());
    channel.updateDescription(dto.newDescription());
    channel.updateUpdatedAt();

    return channelRepository.update(channel);
  }

  @Override
  public void deleteChannel(String channelId) {
    validator.findOrThrow(Channel.class, channelId, new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

    channelRepository.delete(channelId);
  }

  @Override
  public String generateInviteCode(Channel channel) {
    return null;
  }

}
