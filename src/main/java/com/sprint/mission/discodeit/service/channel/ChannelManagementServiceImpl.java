package com.sprint.mission.discodeit.service.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.message.MessageService;
import com.sprint.mission.discodeit.service.user.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelManagementServiceImpl implements ChannelManagementService {

  private final ChannelService channelService;
  private final UserService userService;
  private final ReadStatusService readStatusService;
  private final MessageService messageService;

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Channel createPrivateChannel(Channel channel, List<String> userIds) {

    List<User> participants = userService.findAllUsersIn(userIds);
    log.debug("[FOUND USERS OF IDS] : [{}]", userIds);

    List<ReadStatus> statuses = participants.stream()
        .map(user -> new ReadStatus(channel, user))
        .toList();
    log.debug("[CREATED READ STATUS OF USERS] : [{}]", userIds);

    statuses.stream().forEach(channel::addReadStatus);

    return channelService.createPrivateChannel(channel);
  }

  // affects only one row
  @Override
  public Channel createPublicChannel(Channel channel) {
    return channelService.createPublicChannel(channel);
  }


  @Override
  public Instant getLastMessageTimeForChannel(String channelId) {
    return Optional.ofNullable(messageService.getLatestMessageByChannel(channelId))
        .map(m -> m.getCreatedAt())
        .orElse(Instant.EPOCH);
  }

  @Override
  public List<User> getChannelParticipants(String channelId) {
    List<ReadStatus> participants = readStatusService.findAllByChannelId(channelId);
    return participants.stream().map(ReadStatus::getUser).toList();
  }

  @Override
  public List<Channel> findAllChannelsForUser(String userId) {

    List<ReadStatus> statuses = readStatusService.findAllByUserId(userId);

    List<UUID> extractedChannelIds = parseStatusToChannelUuid(statuses);
    List<Channel> channels = channelService.findAllChannelsInOrPublic(extractedChannelIds);

    List<ReadStatus> secondStatuses = getDifferentReadStatuses(channels, extractedChannelIds);

    List<UUID> newChannelIds = secondStatuses.stream().map(status -> status.getChannel().getId())
        .distinct().collect(Collectors.toList());

    List<Channel> newChannels = channelService.findAllChannelsInOrPublic(newChannelIds);
    List<Channel> mergedChannels = Stream.concat(channels.stream(), newChannels.stream())
        .distinct()
        .collect(Collectors.toList());

    List<ReadStatus> mergedStatuses = Stream.concat(secondStatuses.stream(), statuses.stream())
        .distinct()
        .collect(Collectors.toList());

    List<String> userIds = mergedStatuses.stream()
        .map(status -> status.getUser().getId().toString()).collect(Collectors.toList());
    List<User> users = userService.findAllUsersIn(userIds);

    return mergedChannels;
  }


  private List<UUID> parseStatusToChannelUuid(List<ReadStatus> statuses) {
    return statuses.stream()
        .map(status -> status.getChannel().getId())
        .distinct()
        .collect(Collectors.toList());
  }

  private List<ReadStatus> getDifferentReadStatuses(List<Channel> channels,
      List<UUID> originalChannelIds) {

    List<UUID> difference = channels.stream()
        .map(c -> c.getId())
        .filter(id -> !originalChannelIds.contains(id))
        .collect(Collectors.toList());

    return readStatusService.findAllInChannel(difference);
  }
}
