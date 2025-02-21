package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.status.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.request.status.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.ChannelRepository;
import com.sprint.mission.discodeit.repository.interfacepac.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  public ReadStatusResponseDTO create(ReadStatusCreateDTO createDTO) {
    // user 조회
    User user = userRepository.findById(createDTO.userId())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
    // 채널 조회
    Channel channel = channelRepository.findById(createDTO.channelId())
        .orElseThrow(() -> new IllegalArgumentException("Channel not found"));
    //중복 체크 ? -> 마지막으로 읽은 상태는 하나만 존재 가능, 왜? 데이터 일관성 깨짐, 계산하기 모호.
    if (readStatusRepository.existsByUserAndChannel(user, channel)) {
      throw new IllegalArgumentException(
          "ReadStatus already exists for User: " + user.getId() + " and Channel: "
              + channel.getId());
    }
    //새로운 ReadStatus 객체 생성 및 저장
    ReadStatus readStatus = new ReadStatus(user, channel, createDTO.lastReadAt());
    readStatusRepository.save(readStatus);
    //응답 DTO 변환 후 반환
    return new ReadStatusResponseDTO(
        readStatus.getId(),
        readStatus.getUser().getId(),
        readStatus.getChannel().getId(),
        readStatus.getLastReadAt()
    );
  }

  public ReadStatusResponseDTO find(UUID readStatusId) {
    //ReadStatus 조회
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));

    //ReadStatus DTO로 변환 후 반환
    return new ReadStatusResponseDTO(
        readStatus.getId(),
        readStatus.getUser().getId(),
        readStatus.getChannel().getId(),
        readStatus.getLastReadAt()
    );
  }

  public List<ReadStatusResponseDTO> findAllByUserId(UUID userId) {
    //사용자 조회
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));
    //특정 사용자 모든 readStatus 조회
    List<ReadStatus> readStatusList = readStatusRepository.findAllByUser(user); // 나중에 구현
    //readStatus 리스트 DTO 변환 후 반환
    return readStatusList.stream()
        .map(readStatus -> new ReadStatusResponseDTO(
            readStatus.getId(),
            readStatus.getUser().getId(),
            readStatus.getChannel().getId(),
            readStatus.getLastReadAt()
        ))
        .toList();
  }

  public ReadStatusResponseDTO update(ReadStatusUpdateDTO updateDTO) {
    //readStatus 조회
    ReadStatus readStatus = readStatusRepository.findById(updateDTO.readStatusId())
        .orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));
    //마지막 읽은 시간 업데이트
    readStatus.updateReadTime(updateDTO.lastReadAt());
    //업데이트된 readStatus 저장
    readStatusRepository.save(readStatus);
    //readStatus DTO 변환 후 반환
    return new ReadStatusResponseDTO(
        readStatus.getId(),
        readStatus.getUser().getId(),
        readStatus.getChannel().getId(),
        readStatus.getLastReadAt()
    );
  }

  public void delete(UUID readStatusId) {
    // readStatus 조회
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));
    // 삭제
    readStatusRepository.deleteById(readStatusId);

    log.error("ReadStatus deleted: {}", readStatus);
  }
}
