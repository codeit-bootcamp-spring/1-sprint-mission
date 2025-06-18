package com.spirnt.mission.discodeit.storage.local;

import com.spirnt.mission.discodeit.async.AsyncTaskFailure;
import com.spirnt.mission.discodeit.async.notification.NotificationCreateEvent;
import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.entity.NotificationType;
import com.spirnt.mission.discodeit.exception.BinaryContent.FileException;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.security.CustomUserDetails;
import com.spirnt.mission.discodeit.storage.BinaryContentStorage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;

    public LocalBinaryContentStorage(
        @Value("${discodeit.storage.local.root-path}") Path root,
        ApplicationEventPublisher eventPublisher,
        UserRepository userRepository
    ) {
        this.root = root;
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
    }

//    // Bean 생성 시 자동 호출하여 루트 디렉토리 초기화
//    @PostConstruct
//    void init() {
//        File[] files = root.toFile().listFiles();
//        if (files != null) {
//            for (File file : files) {
//                if (file.isFile()) {
//                    file.delete();
//                }
//            }
//        }
//    }

    /// 파일 실제 저장 위치 정의
    Path resolvePath(UUID binaryContentId) {
        return Paths.get(root + "/" + binaryContentId);
    }

    // 파일을 로컬에 저장
    @Async("propagatingExecutor")
    @Retryable(
        value = {FileException.class},  // FileException 발생 시 재시도
        maxAttempts = 3,    // 최대 시도 횟수
        backoff = @Backoff(delay = 2000) // 2초 대기
    )
    @Override
    public CompletableFuture<UUID> put(UUID binaryContentId, byte[] bytes) {
        Path path = resolvePath(binaryContentId);
        File file = new File(String.valueOf(path));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        } catch (IOException e) {
            log.warn("[스토리지에 파일 저장 중 오류가 발생했습니다.]");
            throw new FileException(
                Map.of("binaryContentId", binaryContentId, "message", e.getMessage()));
        }
        return CompletableFuture.completedFuture(binaryContentId);
    }

    @Recover
    public CompletableFuture<UUID> recover(FileException e, UUID binaryContentId, byte[] bytes) {
        String requestId = MDC.get("requestId");  // MDC에서 requestId 추출
        AsyncTaskFailure failure = new AsyncTaskFailure(
            "binaryContentStorage-put",
            requestId != null ? requestId : "UNKNOWN",
            e.getMessage()
        );

        // 비동기 작업 실패 알림 이벤트
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails userDetails) {
                UUID userId = userDetails.getUser().getId();
                userRepository.findById(userId)
                    .ifPresent(user -> eventPublisher.publishEvent(
                        new NotificationCreateEvent(
                            List.of(user),
                            "비동기 작업 실패",
                            "파일 업로드 작업이 실패했습니다.",
                            NotificationType.ASYNC_FAILED,
                            null
                        )
                    ));
            }
        }

        log.error("[비동기 파일 저장 실패] {}", failure);

        return CompletableFuture.completedFuture(binaryContentId);
    }


    // 파일을 InputStream 데이터 타입으로 반환
    @Override
    public InputStream get(UUID binaryContentId) {
        Path path = resolvePath(binaryContentId);
        try {
            return new FileInputStream(String.valueOf(path));
        } catch (FileNotFoundException e) {
            log.warn("[스토리지에 파일이 없습니다.]");
            throw new FileException(Map.of("binaryContentId", binaryContentId));
        }
    }

    // 파일 다운로드
    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        Path path = resolvePath(binaryContentDto.getId());
        Resource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, binaryContentDto.getContentType())
            .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                .filename(binaryContentDto.getFileName(), StandardCharsets.UTF_8)
                .build()
                .toString())
            .body(resource);
    }
}
