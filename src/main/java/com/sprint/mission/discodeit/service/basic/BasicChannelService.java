package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
//
import com.sprint.mission.discodeit.service.ReadStatusService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
//
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  //
  private final ReadStatusService readStatusService;
  //
  private final ChannelMapper channelMapper;
  //
  private final InputHandler inputHandler;


  @Override
  public ChannelDto createPublicChannel(ChannelPublicRequest request) {
    log.info("공개 채널 생성 시도: channelName={}, channelDescription={} ",
        request.name(),
        request.description());

    Channel channel = Channel.builder()
        .type(ChannelType.PUBLIC)
        .name(request.name())
        .description(request.description())
        .build();
    channelRepository.save(channel);

    log.info("공개 채널 생성 성공: channelName={}, createdAt={}",
        channel.getName(),
        channel.getCreatedAt());
    return channelMapper.toDto(channel);
  }

  @Override
  public ChannelDto createPrivateChannel(ChannelPrivateRequest request) {
    log.info("비공개 채널 생성 시도");

    Channel channel = Channel.builder()
        .type(ChannelType.PRIVATE)
        .build();
    channelRepository.save(channel);

    request.participantIds().stream()
        .map(userId -> ReadStatus.builder()
            .user(userRepository.findById(userId).orElseThrow(() ->
            {
              log.error("비공개 채널 생성 단계에서 유저를 찾지 못함: userId={}", userId);
              return new NoSuchElementException("유저(" + userId + ")가 존재하지 않습니다.");
            }))
            .channel(channelRepository.findById(channel.getId()).orElseThrow(
                () -> {
                  log.error("비공개 채널을 찾지 못함: privateChannelId={}", channel.getId());
                  return new NoSuchElementException("채널(" + channel.getId() + ")이 존재하지 않습니다.");
                }))
            .lastReadAt(channel.getCreatedAt())
            .build()
        )
        .forEach(readStatusRepository::save);

    log.info("비공개 채널 생성 성공");
    return channelMapper.toDto(channel);
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<Channel> channels = readStatusService.findAllReadStatusEntitiesByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .toList();
    return channels.stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Override
  public ChannelDto getChannelById(UUID id) {
    // 특정 채널을 불러오기
    Channel channel = channelRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("해당 채널( " + id + " )이 없습니다."));

    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public ChannelDto updateChannel(UUID id, ChannelUpdateRequest request) {

    Channel channel = channelRepository.findById(id).orElseThrow(
        () -> {
          log.error("채널 수정 단계에서 채널을 찾지 못함: channelId={}", id);
          return new NoSuchElementException("채널(" + id + ")가 존재하지 않습니다.");
        }); // 전역 404

    log.info("채널 수정 시도: originalChannelName={}", channel.getName());

    if (channel.getType() == ChannelType.PRIVATE) {
      log.warn("PRIVATE 채널이라서 수정이 불가함: channelId={}", id);
      throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
    } // 전역 400

    channel.updateName(request.newName());
    channel.updateDescription(request.newDescription());
    // 수정 시간 업데이트
    channel.refreshUpdateAt();

    log.info("채널 수정 시도 성공: channelName={}, updatedAt={}",
        channel.getName(),
        channel.getCreatedAt());
    // JPA 의 더티 채킹으로 save 하지 않아도 DB에 자동 업데이트
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void deleteChannelById(UUID id) {
    log.info("채널 삭제 시도");
    String keyword = inputHandler.getYesNOInput().toLowerCase();
    if (keyword.equals("y")) {

      if (channelRepository.findById(id).isEmpty()) {
        log.error("채널 삭제 단계에서 채널을 찾지 못함: channelId={}", id);
        throw new NoSuchElementException("채널(" + id + ")가 존재하지 않습니다.");
      }

      log.info("채널 메세지 삭제");
      // 채널 메세지 삭제
      List<UUID> messageIds =
          messageRepository.findByChannelId(id).stream()
              .map(BaseEntity::getId)
              .toList();

      // messageRepository::deleteMessageById 메서드 참조
      messageIds.forEach(messageRepository::deleteById);

      log.info("채널의 읽음 상태 삭제");
      // 채널의 읽음 상태 삭제
      List<UUID> readStatuseIds =
          readStatusService.findAllReadStatusEntitiesByUserId(id).stream()
              .map(ReadStatus::getId)
              .toList();

      readStatuseIds.forEach(readStatusService::deleteReadStatusById);
      
      // 채널 삭제
      channelRepository.deleteById(id);
      log.info("채널 삭제 시도 성공");
    }
  }
}
