package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  //
  private final ReadStatusRepository readStatusRepository;
  //
  private final ReadStatusMapper readStatusMapper;


  @Transactional
  @Override
  public ReadStatusDto createReadStatus(ReadStatusCreateRequest request) {

    // TODO 채널하고 와서 채우기
    if (channelRepository.findById(request.channel().getId()).isEmpty()) {
      throw new NoSuchElementException("채널(" + request.channel().getId() + ")이 존재하지 않습니다.");
    }

    if (userRepository.findById(request.user().getId()).isEmpty()) {
      throw new NoSuchElementException("유저(" + request.user().getId() + ")가 존재하지 않습니다.");
    }

    if (readStatusRepository.existsByChannelIdAndUserId(request.channel().getId(),
        request.user().getId())) {
      throw new IllegalArgumentException("ReadStatus는 이미 존재합니다."); // 전역 에러에서 400 처리
    }

    ReadStatus readStatus = ReadStatus.builder()
        .lastReadAt(request.lastReadAt())
        .user(request.user())
        .channel(request.channel())
        .build();

    readStatusRepository.save(readStatus);

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public ReadStatusDto findReadStatusById(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("ReadStatus(" + readStatusId + ")가 존재하지 않습니다."));
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    // TODO 예외 처리
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Override
  public List<ReadStatus> findAllReadStatusEntitiesByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId);
  }

  @Override
  public List<ReadStatusDto> findAllByChannelId(UUID channelId) {
    // TODO 예외 처리
    return readStatusRepository.findByChannelId(channelId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }


  @Transactional
  @Override
  public ReadStatusDto updateReadStatus(
      UUID id, ReadStatusUpdateRequest readStatusUpdateRequest) {

    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Message 읽음 상태를 찾을 수 없습니다."));

    readStatus.updateLastMessageReadAt(readStatusUpdateRequest.lastReadAt());
    readStatus.refreshUpdateAt();

    // JPA 의 더티 채킹으로 save 하지 않아도 DB에 자동 업데이트인데 전 미션 때 save를 빼먹었습니다

    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  @Override
  public void deleteReadStatusById(UUID id) {
    readStatusRepository.deleteById(id);
  }
}
