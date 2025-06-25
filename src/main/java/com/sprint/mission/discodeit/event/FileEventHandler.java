package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageImageDto;
import com.sprint.mission.discodeit.dto.ProfileUploadDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.binaryContent.FileNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.NotificationService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileEventHandler {

  private final BinaryContentStorage binaryContentStorage;
  private final TransactionTemplate transactionTemplate;
  private final BinaryContentRepository binaryContentRepository;
  private final NotificationService notificationService;
  private final UserRepository userRepository;
  private final BinaryContentMapper binaryContentMapper;

  /**
   * 유저의 프로필 생성/ 프로필 수정, 메세지의 이미지 첨부 후 이루어져야하는 작업 : 1. 업로드 성공/실패에 따라서 파일 업로드 상태 변경 2. SSE 알림 전송 로직
   **/

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async
  public void handleProfileUpload(ProfileUploadDto uploadDto) {
    log.info("프로필 이미지 비동기 업로드 시작");
    // 1. binaryContent 저장소에 이미지 업로드
    CompletableFuture<Void> completableFuture = binaryContentStorage.put(uploadDto.id(),
        uploadDto.bytes());

    // 2. whenComplete() 된 후, 성공/실패 여부를 따져 BinaryContent 메타 데이터 수정
    completableFuture.whenComplete(
        (result, exception) -> {
          transactionTemplate.execute(
              status -> {
                if (exception != null) {
                  handleUploadFail(uploadDto.id(), uploadDto.userId());
                } else {
                  handleUploadSuccess(uploadDto.id(), uploadDto.userId());
                }
                return null;
              });
        });
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async
  public void handleMessageImageUpload(MessageImageDto messageImageDto) {
    log.info("메세지 이미지 비동기 업로드 시작");
    // 1. binaryContent 저장소에 이미지 업로드
    for (List<Object> fileInfo : messageImageDto.fileInfoList()) {
      log.info("fileInfoSize={}, ", fileInfo.size());
      if (fileInfo.get(0) instanceof UUID id && fileInfo.get(1) instanceof byte[] bytes
          && fileInfo.get(2) instanceof UUID userId) {
        CompletableFuture<Void> completableFuture = binaryContentStorage.put(id, bytes);

        // 2. whenComplete() 된 후, 성공/실패 여부를 따져 BinaryContent 메타 데이터 수정
        completableFuture.whenComplete(
            (result, exception) -> {
              transactionTemplate.execute(
                  status -> {
                    if (exception != null) {
                      handleUploadFail(id, userId);
                    } else {
                      handleUploadSuccess(id, userId);
                    }
                    return null;
                  });
            });
      }
    }

  }

  private void handleUploadSuccess(UUID id, UUID userId) {
    log.info("파일 업로드 최종 성공, 상태 변경 : binaryContentId={}", id);
    // 상태 변경 로직
    binaryContentRepository.updateStatusById(id, BinaryContentUploadStatus.SUCCESS);
    // SSE 이벤트 전송
    sendUploadSseNotification(id, userId);
  }

  private void handleUploadFail(UUID id, UUID userId) {
    log.info("파일 업로드 최종 실패, 상태 변경 : binaryContentId={}", id);
    // 상태 변경 로직
    binaryContentRepository.updateStatusById(id, BinaryContentUploadStatus.FAILED);
    // SSE 이벤트 전송
    sendUploadSseNotification(userId, id);
  }

  private void sendUploadSseNotification(UUID userId, UUID binaryId) {
    // User 객체 조회
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));
    // BinaryContent 조회 및 BinaryContentDto 객체 생성
    BinaryContent binaryContent = binaryContentRepository.findById(binaryId)
        .orElseThrow(() -> new FileNotFoundException(Map.of("binaryId", binaryId)));
    BinaryContentDto binaryContentDto = binaryContentMapper.toDto(binaryContent);
    // SSE 이벤트 전송 메서드 호출
    notificationService.send(user, "binaryContents.status", binaryContentDto);
  }


}
