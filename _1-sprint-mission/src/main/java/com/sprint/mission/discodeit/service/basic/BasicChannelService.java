package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelCannotBeUpdatedException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service //서비스 Bean
@RequiredArgsConstructor // 생성자
@Slf4j
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto create(PublicChannelCreateRequest publicChannelCreateRequest) {
    log.info("Public Channel 생성시작 - name: {}, description: {}", publicChannelCreateRequest.name(),
        publicChannelCreateRequest.description());

    Channel channel = new Channel(
        ChannelType.PUBLIC,
        publicChannelCreateRequest.name(),
        publicChannelCreateRequest.description()
    );
    channelRepository.save(channel);
    log.info("Public Channel 생성 성공: {}", channel);
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest privateChannelCreateRequest) {
    log.info("Private Channel 생성 시작");
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);
    log.info("Private Channel 생성 성공: {}", channel);

    List<ReadStatus> readStatuses = userRepository.findAllById(
            privateChannelCreateRequest.participantIds())
        .stream()
        .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
        .toList();

    readStatusRepository.saveAll(readStatuses);
    log.info("ReadStatus 저장 성공");
    return channelMapper.toDto(channel);
  }


  @Transactional(readOnly = true)
  @Override
  public ChannelDto findById(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(
            () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
                Map.of("channelId", channelId)));
  }

  //특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가
  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId)
        .stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .toList();

    return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
        .stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest channelUpdateDto) {
    log.info("Channel 업데이트 시작: {}", channelId);
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> {
              log.error("Channel id({})를 찾을 수 없음 ", channelId);
              return new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
                  Map.of("channelId", channelId));
            }
        );

    if (channel.getType().equals(ChannelType.PRIVATE)) {
      log.error("PRIVATE 타입은 업데이트 불가능");
      throw new PrivateChannelCannotBeUpdatedException(ErrorCode.PRIVATE_CHANNEL_CANNOT_UPDATE,
          Map.of("channelId", channelId, "channelType", channel.getType()));
    }

    channel.updateChannel(channelUpdateDto.newName(), channelUpdateDto.newDescription());
    log.info("Channel 수정 성공: {}", channel);
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void deleteById(UUID channelId) {
    log.info("Channel 삭제 시작: {}", channelId);
    if (!channelRepository.existsById(channelId)) {
      log.error("Channel id({})를 찾을 수 없음 ", channelId);
      throw new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
          Map.of("channelId", channelId));
    }

    //관련된 Message 삭제
    messageRepository.deleteAllByChannelId(channelId);

    //관련된 ReadStatus 삭제
    readStatusRepository.deleteAllByChannelId(channelId);

    //채널 삭제
    channelRepository.deleteById(channelId);
    log.info("Channel 삭제 성공: {}", channelId);
  }
}