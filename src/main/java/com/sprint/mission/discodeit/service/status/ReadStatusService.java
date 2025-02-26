package com.sprint.mission.discodeit.service.status;

import com.sprint.mission.discodeit.entity.status.read.ReadStatusModifyRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusModifyResponse;
import com.sprint.mission.discodeit.entity.status.read.ChannelReadStatus;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusCreateResponse;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.status.read.UserReadStatusResponse;
import com.sprint.mission.discodeit.entity.user.UserCommonResponse;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.StatusService;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusService implements StatusService<ReadStatus> {

  private final ReadStatusRepository readStatusRepository;
  private final UserService userService;
  private final ChannelService channelService;

  @Override
  public ReadStatus create(Object request) {
    if (!(request instanceof ReadStatusCreateRequest createRequest)) {
      throw new IllegalArgumentException("ReadStatusCreateRequest 객체가 아닙니다.");
    }
    return convertToReadStatus(createRequest);
  }

  public ChannelReadStatus createChannelReadStatus(String channelId) {
    UUID uuid = convertToUUID(channelId);
    ReadStatus readStatus = find(uuid);
    if (readStatus != null) {
      return new ChannelReadStatus(readStatus.getChannelId(), readStatus.getLastReadAt());
    }

    return new ChannelReadStatus(uuid, Instant.now());
  }

  /**
   * 스프린트 미션 5, message 읽음 상태 생성
   */
  @Override
  public ReadStatusCreateResponse createReadStatus(ReadStatusRequest request) {
    ReadStatus readStatus = ReadStatus.createReadStatus(request.userId(), request.channelId(),
        request.lastReadAt());

    readStatusRepository.save(readStatus);

    return new ReadStatusCreateResponse(readStatus.getId(), readStatus.getCreatedAt(),
        readStatus.getUpdatedAt(), readStatus.getUserId(), readStatus.getChannelId(),
        readStatus.getLastReadAt());
  }

  /**
   * 스프린트 미션 5, user의 메세지 읽음 상태 목록 조회
   */
  @Override
  public List<ReadStatusResponse> findByUserId(UUID userId) {
    List<ReadStatus> allReadStatus = readStatusRepository.findAllReadStatusByUserId(userId);

    return allReadStatus.stream()
        .map(
            read -> new ReadStatusResponse(read.getId(), read.getCreatedAt(), read.getUpdatedAt(),
                read.getUserId(), read.getChannelId(), read.getLastReadAt()))
        .toList();
  }

  /**
   * `id`로 조회합니다.
   */
  @Override
  public ReadStatus find(UUID channelId) {
    return readStatusRepository.findByChannelId(channelId);
  }

  /**
   * userId를 조건으로 전부 조회
   */
  @Override
  public Map<UUID, UserReadStatusResponse> findAllByUserId(String userName) {
    UserCommonResponse findUser = userService.find(userName);
    UUID userId = findUser.id();
    Map<UUID, ReadStatus> allReadStatus = readStatusRepository.findAll();

    if (allReadStatus.isEmpty()) {
      throw new IllegalArgumentException("readStatus를 찾지 못했습니다.");
    }

    // 디버깅을 위한 로그 추가
    System.out.println("Finding ReadStatus for user: " + userName + " (ID: " + userId + ")");
    allReadStatus.values().forEach(status ->
        System.out.println("ReadStatus - ChannelId: " + status.getChannelId() +
            ", UserId: " + status.getUserId())
    );

    return allReadStatus.values().stream()
        .filter(stat -> {
          boolean matches = stat.getUserId().equals(userId);
          System.out.println("Checking ReadStatus - UserId: " + stat.getUserId() +
              " matches user " + userId + ": " + matches);
          return matches;
        })
        .collect(Collectors.toMap(
            ReadStatus::getChannelId,
            s -> new UserReadStatusResponse(s.getChannelId(), s.getLastReadAt())
        ));
  }

  /**
   * DTO를 활용해 파라미터를 그룹화합니다. - 수정 대상 객체의 `id` 파라미터, 수정할 값 파라미터
   */
  @Override
  public ReadStatus update(Object request) {
    if (!(request instanceof ReadStatusUpdateRequest)) {
      throw new IllegalArgumentException("ReadStatusUpdateRequest 객체가 아닙니다.");
    }
    ReadStatusUpdateRequest updateRequest = (ReadStatusUpdateRequest) request;
    System.out.println("updateRequest.channelId() = " + updateRequest.channelId());
    ReadStatus findByChannelId = readStatusRepository.findByChannelId(updateRequest.channelId());

    if (findByChannelId == null) {
      throw new IllegalArgumentException("채널에 대한 정보가 없습니다.");
    }

    findByChannelId.updateLastReadAt();
    return findByChannelId;
  }

  public ChannelReadStatus updateChannelReadStatus(String id) {
    UUID channelId = convertToUUID(id);

    ReadStatus update = readStatusRepository.update(channelId);

    return new ChannelReadStatus(update.getChannelId(), update.getLastReadAt());
  }

  /**
   * 스프린트 미션 5, readStatus id로 수정
   */
  @Override
  public ReadStatusModifyResponse updateReadStatus(UUID readStatusId,
      ReadStatusModifyRequest request) {

    ReadStatus readStatus = readStatusRepository.find(readStatusId);
    readStatus.updateLastReadAt();

    readStatusRepository.save(readStatus);

    return new ReadStatusModifyResponse(readStatus.getLastReadAt());
  }


  /**
   * `id`로 삭제합니다.
   */
  @Override
  public void delete(UUID channelId) {
    ReadStatus findChannelStat = readStatusRepository.findByChannelId(channelId);

    if (findChannelStat == null) {
      throw new IllegalArgumentException("채널에 대한 정보가 없습니다.");
    }

    readStatusRepository.remove(channelId);
  }

  private ReadStatus convertToReadStatus(ReadStatusCreateRequest createRequest) {
    // channel이나 user가 존재하지 않으면 예외
    isNotExistsChannelOrUser(createRequest);

    // 같은 channel과 user와 관련된 객체가 있으면 예외
    isExistsChannelOrUser(createRequest);

    ReadStatus readStatus = ReadStatus.createReadStatus(createRequest.userId(),
        createRequest.channelId());
    readStatusRepository.save(readStatus);

    return readStatus;
  }

  private void isExistsChannelOrUser(ReadStatusCreateRequest createRequest) {
    ReadStatus byChannelId = readStatusRepository.findByChannelId(createRequest.channelId());
    if (byChannelId != null) {
      throw new IllegalArgumentException("생성할 수 없습니다. 이미 채널이 존재합니다.");
    }
  }

  private void isNotExistsChannelOrUser(ReadStatusCreateRequest createRequest) {
    Optional.ofNullable(channelService.findChannelById(createRequest.channelId()))
        .orElseThrow(() -> new ChannelNotFoundException("채널이 존재하지 않습니다."));
    Optional.ofNullable(userService.find(createRequest.userId()))
        .orElseThrow(() -> new UserNotFoundException("유저가 존재하지 않습니다."));
  }

  private UUID convertToUUID(String messageId) {
    if (messageId == null || messageId.length() != 32 || !messageId.matches("[0-9a-fA-F]+")) {
      throw new MessageNotFoundException("messageID를 확인해주세요");
    }

    String uuid = new StringBuilder(messageId)
        .insert(8, "-")
        .insert(13, "-")
        .insert(18, "-")
        .insert(23, "-")
        .toString();

    return UUID.fromString(uuid);
  }
}
