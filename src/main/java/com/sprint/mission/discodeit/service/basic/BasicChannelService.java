package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;

  @Transactional
  @Override
  public ChannelDto create(PublicChannelCreateRequest publicChannelCreateRequest) {
    if (channelRepository.existsByName((publicChannelCreateRequest.channelName()))) {
      throw new IllegalArgumentException("이미 존재하는 채널 이름입니다.");
    }

    Channel channel = new Channel(ChannelType.PUBLIC, publicChannelCreateRequest.channelName(),
        publicChannelCreateRequest.description());
    channelRepository.save(channel);
    return toDto(channel);
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest privateChannelCreateRequest) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel createdChannel = channelRepository.save(channel);

    privateChannelCreateRequest.participantIds().forEach(userId -> {
      User participant = userRepository.findById(userId)
          .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));
      ReadStatus readStatus = new ReadStatus(createdChannel, participant);
      readStatusRepository.save(readStatus);
    });

    return toDto(createdChannel);
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDto find(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));
    return toDto(channel);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findPublicAll() {
    return channelRepository.findAll().stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC)
        .map(this::toDto)
        .toList();
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC)
                || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(this::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId,
      PublicChannelUpdateRequest publicChannelUpdateRequest) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));

    channel.update(publicChannelUpdateRequest.newName(),
        publicChannelUpdateRequest.newDescription());
    channelRepository.save(channel);
    return toDto(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId, UUID adminId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("채널이 존재하지 않습니다.");
    }
    channelRepository.deleteById(channelId);
  }

  @Transactional(readOnly = true)
  @Override
  public List<UUID> getParticipantIds(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      return readStatusRepository.findAllByChannelId(channelId)
          .stream()
          .map(readStatus -> readStatus.getChannel().getId())
          .toList();
    }
    return List.of();
  }

  private Instant getLastMessageAt(UUID channelId) {
    return messageRepository.findByChannelId(channelId)
        .stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.MIN);
  }

  private ChannelDto toDto(Channel channel) {
    return ChannelDto.fromEntity(channel, getParticipantIds(channel.getId()),
        getLastMessageAt(channel.getId()));
  }

}
