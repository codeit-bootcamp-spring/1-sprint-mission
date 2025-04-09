package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.execption.channel.CannotUpdatePrivateChannelException;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;

  private final ChannelValidator channelValidator;
  private final UserValidator userValidator;

  private final ChannelMapper channelMapper;

  @Override
  public ChannelDto createPublicChannel(CreatePublicChannelRequest createPublicChannelRequest) {
    Channel channel = channelMapper.toEntity(createPublicChannelRequest.name(),
        createPublicChannelRequest.description(), ChannelType.PUBLIC);

    return channelMapper.toChannelDto(channelRepository.saveChannel(channel));
  }

  @Override
  @Transactional
  public ChannelDto createPrivateChannel(CreatePrivateChannelRequest createPrivateChannelRequest) {
    Channel channel = channelRepository.saveChannel(
        channelMapper.toEntity(null, null, ChannelType.PRIVATE)
    );

    // 유저 초대 및 ReadStatus 생성
    createPrivateChannelRequest.participantIds().forEach(
        userId -> {
          // 이 동작은 readStatusService의 createReadStatus 메서드와 동작이 정확히 일치한다.
          // 하지만 이 동작 하나만을 위해 readStatusService를 추가하는 것이 정말 맞는 것일까?
          User user = userValidator.validateUserExistsByUserId(userId);
          readStatusRepository.saveReadStatus(ReadStatus.of(user, channel, Instant.now()));
        }
    );

    return channelMapper.toChannelDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelDto findChannelByIdOrThrow(UUID channelId) {
    Channel channel = channelValidator.validateChannelExistsByChannelId(channelId);

    return channelMapper.toChannelDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllChannelsByUserId(UUID userId) {
    // PUBLIC + User가 참가한 PRIVATE 채널
    User user = userValidator.validateUserExistsByUserId(userId);

    List<UUID> channelIdList = readStatusRepository.findAllReadStatusByUser(user).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();

    List<Channel> channelList = channelRepository.findAllChannels().stream()
        .filter(channel ->
            channel.getChannelType().equals(ChannelType.PUBLIC)
                || channelIdList.contains(channel.getId()))
        .toList();

    return channelList.stream()
        .map(channel -> findChannelByIdOrThrow(channel.getId()))
        .toList();
  }

  @Override
  @Transactional
  public ChannelDto updateChannel(UUID channelId, UpdateChannelRequest updateChannelRequest) {
    Channel foundChannel = channelValidator.validateChannelExistsByChannelId(channelId);

    if (foundChannel.isPrivate()) {
      throw new CannotUpdatePrivateChannelException(Map.of("channelId", foundChannel.getId()));
    }

    foundChannel.updateChannelInfo(updateChannelRequest.newName(),
        updateChannelRequest.newDescription());

    return channelMapper.toChannelDto(foundChannel);
  }

  @Override
  @Transactional
  public void deleteChannel(UUID channelId) {
    channelValidator.validateChannelExistsByChannelId(channelId);

    // cascade 옵션 message, readStatus 자동 삭제
    channelRepository.removeChannel(channelId);
  }
}
