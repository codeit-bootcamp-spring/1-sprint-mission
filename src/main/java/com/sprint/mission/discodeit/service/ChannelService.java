package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatusDto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
    Channel channel = Channel.publicChannel(request.name(), request.description());
    channelRepository.save(channel);
    return channelMapper.toDto(channel);
  }

  public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
    Channel channel = Channel.privateChannel();
    List<User> users = userRepository.findAllById(request.participantIds());

    if (users.size() != request.participantIds().size()) {
      throw new NoSuchElementException("One or more users not found");
    }
    // readStatus 생성
    List<ReadStatus> readStatuses = users.stream()
        .map(user -> new ReadStatus(user, channel, Instant.now()))
        .toList();
    readStatusRepository.saveAll(readStatuses);

    channelRepository.save(channel);
    return channelMapper.toDto(channel);
  }

  @Transactional(readOnly = true)
  public ChannelDto findById(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(() -> new ResourceNotFoundException("Channel not found: " + channelId));
  }

  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> privateChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();
    return channelRepository.findAll().stream()
        .filter(channel -> channel.isPublic() || privateChannelIds.contains(channel.getId()))
        .map(channelMapper::toDto)
        .toList();
  }

  public ChannelDto updatePublicChannel(UUID channelId, PublicChannelUpdateRequest request) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ResourceNotFoundException("Channel not found: " + channelId));
    if (channel.getName() == null) {
      throw new InvalidResourceException("Invalid channel type: " + channel.getType());
    }
    channel.update(request.newName(), request.newDescription());
    return channelMapper.toDto(channelRepository.save(channel));
  }

  public void delete(UUID channelId) {
    channelRepository.findById(channelId)
        .orElseThrow(() -> new ResourceNotFoundException("Channel not found: " + channelId));
    readStatusRepository.findAllByChannelId(channelId);
    channelRepository.deleteById(channelId);
  }
}
