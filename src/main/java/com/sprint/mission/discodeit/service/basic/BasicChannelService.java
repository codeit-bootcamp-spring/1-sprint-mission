package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.BadRequestException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.jpa.ChannelRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;

  private final ChannelValidator channelValidator;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;


  @Override
  public ChannelDto create(ChannelCreatePublicDTO dto) {
    channelValidator.validateChannel(dto.getName(), dto.getDescription());
    Channel channel = new Channel(dto.getName(), dto.getDescription(), ChannelType.PUBLIC);
    channelRepository.save(channel);
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional
  public ChannelDto create(ChannelCreatePrivateDTO dto) {
    Channel channel = new Channel(null, null, ChannelType.PRIVATE);
    channelRepository.save(channel);

    dto.getParticipantIds().stream()
        .map(userId -> {
          User findUser = userRepository.findById(userId)
              .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
          return new ReadStatus(findUser, channel, Instant.EPOCH);
        })
        .forEach(readStatusRepository::save);

    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelDto find(UUID id) {
    Channel findChannel = channelRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    return channelMapper.toDto(findChannel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<Channel> channels = readStatusRepository.findAllByUser_Id(userId).stream()
        .map(ReadStatus::getChannel).toList();

    return channelRepository.findAll().stream()
        .filter(ch -> ch.getChannelType().equals(ChannelType.PUBLIC)
            || channels.contains(ch))
        .map(channelMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public ChannelDto update(UUID id, ChannelUpdateDTO dto) {
    Channel findChannel = channelRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

    if (findChannel.getChannelType() == ChannelType.PRIVATE) {
      throw new BadRequestException(ErrorCode.PRIVATE_CHANNEL_IMMUTABLE);
    }
    findChannel.setChannel(dto.getNewName(), dto.getNewDescription());
    return channelMapper.toDto(findChannel);
  }

  @Override
  public void delete(UUID id) {
    channelRepository.deleteById(id);
  }

}
