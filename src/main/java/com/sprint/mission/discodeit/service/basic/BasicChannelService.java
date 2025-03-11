package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.ChannelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static com.sprint.mission.discodeit.entity.ChannelType.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {


  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelMapper channelMapper;


  @Override
  @Transactional
  public ChannelDto createPublicChannel(PublicChannelCreateRequestDto request) {
    Channel channel = new Channel(PUBLIC, request.getName(), request.getDescription());
    return channelMapper.toDto(channelRepository.save(channel));
  }

  @Override
  @Transactional
  public ChannelDto createPrivateChannel(PrivateChannelCreateRequestDto request) {
    List<User> participants = userRepository.findAllById(request.getParticipantIds());

    if (participants.size() != request.getParticipantIds().size()) {
      throw new IllegalArgumentException("user not found");
    }

    Channel channel = new Channel(PRIVATE, null, null);
    Channel savedChannel = channelRepository.save(channel);

    participants.forEach(user -> {
      ReadStatus readStatus = new ReadStatus(user, savedChannel, Instant.now());
      readStatusRepository.save(readStatus);
    });

    return channelMapper.toDto(channelRepository.save(channel));
  }

  @Override
  public ChannelDto getChannelById(UUID id) {
    return channelRepository.findById(id)
        .map(channelMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("Channel whith id " + " not found"));
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    return channelRepository.findAllByUserId(userId)
        .stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public ChannelDto updateChannel(UUID id, ChannelUpdateRequestDto request) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Channel not found"));

    if (channel.getType().equals(PRIVATE)) {
      throw new IllegalStateException("Cannot update private channels");
    }
    channel.update(request.getNewName(), request.getNewDescription());
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional
  public void deleteChannel(UUID id) {
    if (!channelRepository.existsById(id)) {
      throw new NoSuchElementException("Channel not found");
    }
    channelRepository.deleteById(id);
  }
}
