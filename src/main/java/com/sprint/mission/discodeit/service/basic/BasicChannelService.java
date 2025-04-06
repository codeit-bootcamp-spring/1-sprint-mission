package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    log.info("공개 채널 생성 요청 - name: {}", request.name());
    
    Channel channel = new Channel(request.name(), ChannelType.PUBLIC);
    channelRepository.save(channel);
    
    log.info("공개 채널 생성 완료 - channelId: {}, name: {}", channel.getId(), channel.getName());
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    log.info("비공개 채널 생성 요청 - userId: {}", request.userId());
    
    Channel channel = new Channel(ChannelType.PRIVATE);
    channelRepository.save(channel);
    
    log.info("비공개 채널 생성 완료 - channelId: {}", channel.getId());
    return channelMapper.toDto(channel);
  }

  @Override
  public ChannelDto find(UUID channelId) {
    log.debug("채널 조회 - channelId: {}", channelId);
    
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(() -> {
          log.error("채널 조회 실패 - 존재하지 않는 channelId: {}", channelId);
          return new ChannelException.ChannelNotFoundException("Channel with id " + channelId + " not found");
        });
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    log.debug("사용자의 채널 목록 조회 - userId: {}", userId);
    return channelRepository.findAllByUserId(userId)
        .stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    log.info("채널 정보 수정 요청 - channelId: {}, name: {}", channelId, request.name());
    
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> {
          log.error("채널 수정 실패 - 존재하지 않는 channelId: {}", channelId);
          return new ChannelException.ChannelNotFoundException("Channel with id " + channelId + " not found");
        });

    if (channel.getType().equals(ChannelType.PRIVATE)) {
      log.error("채널 수정 실패 - 비공개 채널 수정 시도: {}", channelId);
      throw new ChannelException.PrivateChannelUpdateException("Private channel cannot be updated");
    }

    channel.update(request.name());
    log.info("채널 정보 수정 완료 - channelId: {}, name: {}", channelId, request.name());
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    log.info("채널 삭제 요청 - channelId: {}", channelId);
    
    if (!channelRepository.existsById(channelId)) {
      log.error("채널 삭제 실패 - 존재하지 않는 channelId: {}", channelId);
      throw new ChannelException.ChannelNotFoundException("Channel with id " + channelId + " not found");
    }

    channelRepository.deleteById(channelId);
    log.info("채널 삭제 완료 - channelId: {}", channelId);
  }
}
