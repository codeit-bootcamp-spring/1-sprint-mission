package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.persistence.EntityExistsException;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  private final ReadStatusMapper readStatusMapper;
  private final UserMapper userMapper;

  //PRIVATE 채널시에만 수행
  @Override
  public ReadStatusDto create(ReadStatusRequest readStatusRequest) {

    User user = userRepository.findById(readStatusRequest.userId()).orElseThrow(
        () -> new NoSuchElementException("user not found"));

    Channel channel = channelRepository.findById(readStatusRequest.channelId()).orElseThrow(
        () -> new NoSuchElementException("channel not found"));

    //TODO: lastReadAt의 전달시점 고려
    ReadStatus readStatus = ReadStatus.builder()
        .user(user)
        .channel(channel)
        .lastReadAt(Instant.now())
        .build();

    // 이미 채널id와 userId 쌍이 동일한 readStatus가 존재할시 예외
    if (readStatusRepository.findAll().stream()
        .anyMatch(
            readStatus1 -> readStatus1.getChannel().getId().equals(readStatus.getChannel().getId())
                && readStatus1.getUser().getId().equals(readStatus.getUser().getId()))) {
      throw new EntityExistsException("해당 Channel과 User에 대한 ReadStatus가 이미 존재합니다.");
    }

    return readStatusMapper.toDto(readStatusRepository.save(readStatus));
  }

  @Override
  public ReadStatusDto findbyId(UUID uuid) {
    ReadStatus readStatus = readStatusRepository.findById(uuid).orElseThrow(
        () -> new NoSuchElementException("ReadStatus not found"));
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public ReadStatusDto update(ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusUpdateRequest.id()).orElseThrow(
        () -> new NoSuchElementException("ReadStatus not found")
    );
    readStatus.update();
    return readStatusMapper.toDto(readStatusRepository.save(readStatus));
  }

  @Override
  public void delete(UUID uuid) {
    readStatusRepository.deleteById(uuid);
  }

  @Override
  public void deleteByChannelId(UUID id) {
    readStatusRepository.deleteByChannelId(id);
  }

}
