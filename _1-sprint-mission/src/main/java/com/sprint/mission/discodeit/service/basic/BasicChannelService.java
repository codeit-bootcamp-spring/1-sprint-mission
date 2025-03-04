package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service //서비스 Bean
@RequiredArgsConstructor // 생성자
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;

  @Override
  public Channel create(PublicChannelCreateRequest publicChannelCreateRequest) {
    Channel channel = new Channel(
        ChannelType.PUBLIC,
        publicChannelCreateRequest.channelName(),
        publicChannelCreateRequest.description()
    );
    return channelRepository.save(channel);
  }

  @Override
  public Channel create(PrivateChannelCreateRequest privateChannelCreateRequest) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel createdChannel = channelRepository.save(channel);

    privateChannelCreateRequest.participantIds().stream()
        .map(userId -> new ReadStatus(userId, createdChannel.getId(), Instant.MIN))
        .forEach(readStatus -> readStatusRepository.save(readStatus));

    return createdChannel;
  }


  @Override
  public ChannelDto findById(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channel -> toDto(channel))
        .orElseThrow(() -> new NoSuchElementException("Channel not found"));
  }

  //특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannelId)
        .toList();
    return channelRepository.findAll().stream()
        .filter(channel -> channel.getChannelType().equals(ChannelType.PUBLIC)
            || mySubscribedChannelIds.contains(channel.getId()))
        .map(channel -> toDto(channel))
        .toList();
  }

  @Override
  public Channel update(UUID channelId, PublicChannelUpdateRequest channelUpdateDto) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("Channel not found"));
    if (channel.getChannelType() == ChannelType.PRIVATE) {
      throw new UnsupportedOperationException("Private channel cannot be updated");
    }
    channel.updateChannel(channelUpdateDto.newChannelName(), channelUpdateDto.newDescription());
    return channelRepository.save(channel);
  }

  @Override
  public void deleteById(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));

    //관련된 Message 삭제
    messageRepository.deleteAllByChannelId(channel.getId());

    //관련된 ReadStatus 삭제
    readStatusRepository.deleteAllByChannelId(channel.getId());

    //채널 삭제
    channelRepository.deleteById(channelId);

  }

  //가장 최근 메시지 시간을 구하는 메서드
  public Instant getLastMessageTime(Channel channel) {
    Instant latestMessageTime = messageRepository.findAllByChannelId(channel.getId())
        .stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.MIN);
    return latestMessageTime;
  }

  //entity->dto
  public ChannelDto toDto(Channel channel) {
    Instant lastMessageAt = getLastMessageTime(channel);
    List<UUID> participantIds = new ArrayList<>();
    if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId())
          .stream()
          .map(ReadStatus::getUserId)
          .forEach(participantIds::add);
    }
    return new ChannelDto(
        channel.getId(),
        channel.getChannelType(),
        channel.getChannelName(),
        channel.getDescription(),
        participantIds,
        lastMessageAt
    );
  }
}