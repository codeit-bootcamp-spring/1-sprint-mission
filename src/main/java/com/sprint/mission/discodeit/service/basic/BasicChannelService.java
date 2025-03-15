package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.ReadStatusRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validation.ChannelValidator;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ChannelValidator channelValidator;
  private final ChannelMapper channelMapper;
  private final MessageRepository messageRepository;
  private final ReadStatusService readStatusService;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public ChannelResponse createPublicChannel(ChannelRequest.CreatePublic request) {
    if (channelValidator.isValidName(request.name())) {
      Channel newChannel = Channel.createChannel(Channel.ChannelType.PUBLIC, request.name(),
          request.description());
      channelRepository.save(newChannel);

      log.info("Create Public Channel: {}", newChannel);
      return channelMapper.entityToDto(newChannel);
    }
    return null;
  }

  @Override
  @Transactional
  public ChannelResponse createPrivateChannel(ChannelRequest.CreatePrivate request) {
    Channel newChannel = Channel.createChannel(Channel.ChannelType.PRIVATE, null, null);
    channelRepository.save(newChannel);

    for (UUID userId : request.participantIds()) {
      readStatusService.create(
          new ReadStatusRequest.Create(userId, newChannel.getId(), newChannel.getCreatedAt()));
    }

    log.info("Create Private Channel: {}", newChannel);
    return channelMapper.entityToDto(newChannel);
  }

  @Override
  public List<ChannelResponse> findAllByUserId(UUID userId) { // N + 1;
    User user = userRepository.findById(userId).orElseThrow(() ->
        new RestApiException(ErrorCode.USER_NOT_FOUND, "id: " + userId));

    return channelRepository.findAllByUserIdOrType(userId, ChannelType.PUBLIC).stream()
        .map(channelMapper::entityToDto)
        .collect(Collectors.toList());
  }

  @Override
  public ChannelResponse findById(UUID id) {
    return channelMapper.entityToDto(findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public ChannelResponse update(UUID id, ChannelRequest.Update request) {
    Channel channel = findByIdOrThrow(id);

    if (channel.getType() == Channel.ChannelType.PRIVATE) {
      throw new RestApiException(ErrorCode.PRIVATE_CHANNEL_CANNOT_BE_MODIFIED, "id : " + id);
    }

    if (channelValidator.isValidName(request.name()) && channelValidator.isValidName(
        request.description())) {
      Optional.ofNullable(request.name()).ifPresent(channel::updateName);
      Optional.ofNullable(request.description()).ifPresent(channel::updateDescription);

      log.info("Update Channel : {}", channel);
      return channelMapper.entityToDto(channel);
    }
    return null;
  }

  @Override
  public void deleteById(UUID id) {
    channelRepository.deleteById(id);
  }

  private Channel findByIdOrThrow(UUID id) {
    return channelRepository.findById(id)
        .orElseThrow(() -> new RestApiException(ErrorCode.CHANNEL_NOT_FOUND, "id : " + id));
  }

//  private Instant getLastMessageTime(UUID id) {
//    List<Message> channelMessages = messageRepository.findAllByChannelId(id);
//    if (channelMessages.isEmpty()) {
//      return null;
//    }
//    return channelMessages.stream()
//        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
//        .findFirst().get().getCreatedAt();
//  }
//
//  private List<UUID> findJoinUsersById(UUID id) {
//    return readStatusService.findAllByChannelId(id).stream().map(ReadStatusResponse::channelId)
//        .collect(Collectors.toList());
//  }
}
