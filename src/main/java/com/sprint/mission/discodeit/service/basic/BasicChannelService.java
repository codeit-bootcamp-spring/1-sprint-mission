package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserService userService;

  @Override
  @Transactional
  public ChannelResponse createChannel(CreateChannelRequest request) {
    Channel channel = channelRepository.save(new Channel(request.channelName(), false));
    return ChannelResponse.fromEntity(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelResponse> getChannelsResponse() {
    return channelRepository.findAll().stream().map(ChannelResponse::fromEntity).toList();
  }


  @Override
  @Transactional(readOnly = true)
  public ChannelResponse getChannelResponse(UUID uuid) {
    return channelRepository.findById(uuid).map(ChannelResponse::fromEntity)
        .orElseThrow(EntityNotFoundException::new);
  }

  @Override
  @Transactional(readOnly = true)
  public Channel getChannel(UUID uuid) {
    return channelRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  @Transactional
  public ChannelResponse addMessageToChannel(UUID channelUUID, Message message) {
    Channel channel = channelRepository.findById(channelUUID)
        .orElseThrow(EntityNotFoundException::new);
    channel.addMessageToChannel(message);
    channelRepository.save(channel);
    return ChannelResponse.fromEntity(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Message> getMessagesFromChannel(UUID uuid) {
    return channelRepository.findById(uuid).orElseThrow().getMessages();
  }

  @Override
  @Transactional
  public ChannelResponse updateChannel(UUID uuid, UpdateChannelRequest request) {
    Channel channel = channelRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);
    channel.updateChannelName(request.newName());
    channelRepository.save(channel);
    return ChannelResponse.fromEntity(channel);
  }

  @Override
  @Transactional
  public void deleteChannel(UUID uuid) {
    channelRepository.findById(uuid).ifPresent(channelRepository::delete);
  }

  @Override
  @Transactional
  public ChannelResponse createPrivateChannel(CreatePrivateChannelRequest request) {
    Channel channel = new Channel(true);

    List<User> users = request.userIds().stream().map(userService::getUserById).toList();

    users.forEach(channel::addUserToChannel);

    return ChannelResponse.fromEntity(channelRepository.save(channel));
  }
}
