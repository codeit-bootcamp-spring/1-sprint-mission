package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.constant.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;

  @Override
  public UUID createPublicChannel(PublicChannelCreateRequest channelDto) {
    Channel channel = Channel.ofPublic(channelDto.name(), channelDto.description());
    channelRepository.save(channel);
    return channel.getId();
  }

  @Override
  public UUID createPrivateChannel(PrivateChannelCreateRequest privateChannelDto) {
    Channel channel = Channel.ofPrivate(privateChannelDto.ownerId(),
        privateChannelDto.memberIds());
    privateChannelDto.memberIds().forEach(m -> {
      ReadStatus readStatus = new ReadStatus(m, channel.getId());
      readStatusRepository.save(readStatus);
    });
    channelRepository.save(channel);
    return channel.getId();
  }

  @Override
  public ChannelDto findById(UUID channelId) {
    Channel channel = channelRepository.findById(channelId).orElseThrow(() ->
        new NoSuchElementException("channel not found: " + channelId));
    List<Message> messages = messageRepository.findAllByChannel(channelId);
    int lastMessageIndex = messageRepository.findAllByChannel(channelId).lastIndexOf(messages);
    if (lastMessageIndex < 1) {
      return new ChannelDto(channel.getId(), channel.getName(), channel.getDescription(),
          null, channel.getMemberIds());
    } else {
      Long lastMessageTime = messageRepository.findAllByChannel(channelId).get(lastMessageIndex)
          .getUpdatedAt();
      return new ChannelDto(channel.getId(), channel.getName(), channel.getDescription(),
          lastMessageTime, channel.getMemberIds());
    }

  }

  @Override
  public ChannelDto findByName(String name) {
    Channel channel = channelRepository.findByName(name).orElseThrow(() ->
        new NoSuchElementException("channel not found: " + name));

    if (messageRepository.findAllByChannel(channel.getId()).isEmpty()) {
      return new ChannelDto(channel.getId(), channel.getName(), channel.getDescription(), null,
          channel.getMemberIds());
    }

    int lastMessageIndex = messageRepository.findAllByChannel(channel.getId()).size() - 1;
    Long lastMessageTime = messageRepository.findAllByChannel(channel.getId()).get(lastMessageIndex)
        .getUpdatedAt();
    return new ChannelDto(channel.getId(), channel.getName(), channel.getDescription(),
        lastMessageTime, channel.getMemberIds());
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<ChannelDto> channelDtos = new ArrayList<>();
    channelRepository.findAll()
        .forEach(c -> {
          if (c.getType().equals(ChannelType.PUBLIC) || c.getMemberIds().contains(userId)) {
            channelDtos.add(findById(c.getId()));
          }
        });
    return new ArrayList<>(channelDtos);
  }

  @Override
  public void updateName(UUID id, String newName) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("channel not found: " + id));
    channel.updateName(newName);
    channelRepository.save(channel);
  }

  @Override
  public void updateMember(PrivateChannelUpdateRequest channelDto) {
    Channel channel = channelRepository.findById(channelDto.channelId())
        .orElseThrow(
            () -> new NoSuchElementException("channel not found: " + channelDto.channelId()));
    channel.updateMembers(channelDto.memberIds());
    channelRepository.save(channel);
  }

  @Override
  public void remove(UUID channelId, UUID ownerId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("channel not found: " + channelId));
    if (channel.getType().equals(ChannelType.PUBLIC)) {
      readStatusRepository.remove(channelId);
      messageRepository.findAllByChannel(channelId)
          .forEach(m -> messageRepository.remove(m.getId()));
      channelRepository.remove(channelId);
    } else if (channel.getOwnerId().equals(ownerId)) {
      readStatusRepository.remove(channelId);
      messageRepository.findAllByChannel(channelId)
          .forEach(m -> messageRepository.remove(m.getId()));
      channelRepository.remove(channelId);
    } else {
      throw new IllegalArgumentException("삭제 권한이 없는 사용자입니다.");
    }
  }
}