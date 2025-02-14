package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.FindChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdatePrivateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.constant.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
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
  
  @Override
  public void createPublicChannel(UUID ownerId, String name, String description) {
    Channel channel = Channel.ofPublic(ownerId, name, description);
    channelRepository.save(channel);
  }
  
  @Override
  public void createPrivateChannel(CreatePrivateChannelDto privateChannelDto) {
    Channel channel = Channel.ofPrivate(privateChannelDto.ownerId(), privateChannelDto.memeberIds());
    privateChannelDto.memeberIds().forEach(m -> {
      ReadStatus readStatus = new ReadStatus(m, channel.getId());
      readStatusRepository.save(readStatus);
    });
    channelRepository.save(channel);
  }
  
  @Override
  public FindChannelDto findById(UUID channelId) {
    Channel channel = channelRepository.findById(channelId).orElseThrow(() ->
        new NoSuchElementException("channel not found: " + channelId));
    int lastMessageIndex = messageRepository.findAllByChannel(channelId).lastIndexOf(this);
    Long lastMessageTime = messageRepository.findAllByChannel(channelId).get(lastMessageIndex).getUpdatedAt();
    return new FindChannelDto(channel.getId(), channel.getName(), channel.getDescription(), lastMessageTime, channel.getMemberIds());
  }
  
  @Override
  public FindChannelDto findByName(String name) {
    Channel channel = channelRepository.findByName(name).orElseThrow(() ->
        new NoSuchElementException("channel not found: " + name));
    
    if (messageRepository.findAllByChannel(channel.getId()).isEmpty()) {
      return new FindChannelDto(channel.getId(), channel.getName(), channel.getDescription(), null, channel.getMemberIds());
    }
    
    int lastMessageIndex = messageRepository.findAllByChannel(channel.getId()).size() - 1;
    Long lastMessageTime = messageRepository.findAllByChannel(channel.getId()).get(lastMessageIndex).getUpdatedAt();
    return new FindChannelDto(channel.getId(), channel.getName(), channel.getDescription(), lastMessageTime, channel.getMemberIds());
  }
  
  @Override
  public List<FindChannelDto> findAllByUserId(UUID userId) {
    List<FindChannelDto> findChannelDtos = new ArrayList<>();
    channelRepository.findAll()
        .forEach(c -> {
          if (c.getType().equals(ChannelType.PUBLIC) || c.getMemberIds().contains(userId)) {
            findChannelDtos.add(findById(c.getId()));
          }
        });
    return new ArrayList<>(findChannelDtos);
  }
  
  @Override
  public void updateName(UUID id, String newName) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("channel not found: " + id));
    channel.updateName(newName);
    channelRepository.save(channel);
  }
  
  @Override
  public void updateMember(UpdatePrivateChannelDto channelDto) {
    Channel channel = channelRepository.findById(channelDto.channelId())
        .orElseThrow(() -> new NoSuchElementException("channel not found: " + channelDto.channelId()));
    channel.updateMembers(channelDto.memberIds());
    channelRepository.save(channel);
  }
  
  @Override
  public void remove(UUID channelId, UUID ownerId) {
    if (channelRepository.findById(channelId)
        .map(channel -> channel.getOwnerId().equals(ownerId)).isPresent()) {
      readStatusRepository.remove(channelId);
      messageRepository.findAllByChannel(channelId)
          .forEach(m -> messageRepository.remove(m.getId()));
      channelRepository.remove(channelId);
    }
    throw new IllegalArgumentException("삭제 권한이 없는 사용자입니다.");
  }
}