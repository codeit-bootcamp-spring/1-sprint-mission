package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReadStatusServiceImpl implements ReadStatusService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final PermissionService permissionService;

  @Override
  @Transactional
  @CacheEvict(cacheNames = "userChannelList", allEntries = true)
  public ReadStatus create(CreateReadStatusDto dto, UserDetails details) {

    if (!permissionService.checkIsMe(UUID.fromString(dto.userId()), details)) {
      throw new DiscodeitException(ErrorCode.ACCESS_DENIED);
    }

    Channel channel = channelRepository.findById(UUID.fromString(dto.channelId())).orElseThrow(
        () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
            Map.of("channelId", dto.channelId()))
    );

    User user = userRepository.findById(UUID.fromString(dto.userId())).orElseThrow(
        () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("userId", dto.userId()))
    );

    ReadStatus status = new ReadStatus(channel, user);

    if (channel.getType().equals(ChannelType.PRIVATE)) {
      status.enableNotification();
    } else {
      status.disableNotification();
    }

    status.updateLastReadAt(dto.lastReadAt());
    return readStatusRepository.save(status);
  }


  @Override
  public ReadStatus find(String id) {
    return readStatusRepository.findById(UUID.fromString(id)).orElseThrow(
        () -> {
          log.debug("[READ_STATUS NOT FOUND] : [ID: {}]", id);
          return new DiscodeitException(ErrorCode.READ_STATUS_NOT_FOUND,
              Map.of("readStatusId", id));
        }
    );
  }

  /*
  userId 로 user 의 read status -> 불러온 read status 에 있는 channel ID 로 다른 유저의 read status
  쿼리 2번
   */
  @Override
//  @Cacheable(cacheNames = "userReadStatus", key = "#userId")
  public List<ReadStatus> findAllByUserId(String userId) {
    List<ReadStatus> userStatuses = readStatusRepository.findAllByUser_Id(UUID.fromString(userId));
    List<UUID> channelIds = userStatuses.stream().map(status -> status.getChannel().getId())
        .collect(Collectors.toList());

    List<ReadStatus> userStatuses2 = readStatusRepository.findAllByChannel_IdIn(channelIds);

    Set<ReadStatus> merged = new HashSet<>();
    merged.addAll(userStatuses);
    merged.addAll(userStatuses2);

    return userStatuses;
  }

  @Override
  public List<ReadStatus> findAllInChannel(List<UUID> channelIds) {
    return readStatusRepository.findAllByChannel_IdIn(channelIds);
  }

  @Override
  public List<ReadStatus> findAllByChannelId(String channelId) {

    return readStatusRepository.findAllByChannel_Id(UUID.fromString(channelId));
  }

  @Override
  public ReadStatus updateById(UpdateReadStatusDto readStatusDto, String id, UserDetails details) {

    ReadStatus status = readStatusRepository.findById(UUID.fromString(id)).orElseThrow(
        () -> {
          log.info("[READ_STATUS NOT FOUND] : [ID: {}]", id);
          return new DiscodeitException(ErrorCode.READ_STATUS_NOT_FOUND,
              Map.of("readStatusId", id));
        }
    );
    UUID userId = status.getUser().getId();

    if (!permissionService.checkIsMe(userId, details)) {
      throw new DiscodeitException(ErrorCode.ACCESS_DENIED);
    }

    status.updateLastReadAt(readStatusDto.newLastReadAt());
    readStatusRepository.save(status);

    return status;
  }
}
