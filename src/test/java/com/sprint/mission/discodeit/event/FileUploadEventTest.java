package com.sprint.mission.discodeit.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.sprint.mission.discodeit.async.event.FileUploadEvent;
import com.sprint.mission.discodeit.async.failure.AsyncTaskFailure;
import com.sprint.mission.discodeit.async.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContent.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FileUploadEventTest {

  @Autowired
  private BinaryContentRepository binaryContentRepository;

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private PlatformTransactionManager transactionManager;

  // 테스트 간 충돌 방지를 위한 명시적 트랜잭션 관리
  private final TransactionTemplate transactionTemplate;
  @Autowired
  private AsyncTaskFailureRepository asyncTaskFailureRepository;

  public FileUploadEventTest() {
    this.transactionTemplate = new TransactionTemplate(transactionManager);
    this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
  }

  @Test
  @DisplayName("업로드 성공 시 상태가 SUCCESS로 업데이트 되어야 함")
  void shouldUpdateStatusToSuccessOnUploadSuccess() throws Exception {
    // given
    UUID fileId = UUID.randomUUID();
    MockMultipartFile file = createTestFile("success.txt", "success");

    BinaryContent savedContent = transactionTemplate.execute(status -> {
      BinaryContent content = new BinaryContent(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getSize()
      );
      ReflectionTestUtils.setField(content, "id", fileId);

      return binaryContentRepository.saveAndFlush(content);
    });

    // when
    eventPublisher.publishEvent(new FileUploadEvent(fileId, file));

    // then
    await()
        .atMost(5, TimeUnit.SECONDS)
        .pollInterval(500, TimeUnit.MILLISECONDS)
        .untilAsserted(() -> {
          transactionTemplate.executeWithoutResult(__ -> {
            BinaryContent updated = binaryContentRepository.findById(fileId)
                .orElseThrow();

            // 영속성 컨텍스트 강제 갱신
            entityManager.refresh(updated);

            assertThat(updated.getUploadStatus())
                .isEqualTo(BinaryContentUploadStatus.SUCCESS);
          });
        });
  }

  @Test
  @DisplayName("업로드 실패 시 상태가 FAILED로 업데이트 되어야 함")
  void shouldUpdateStatusToFailedOnUploadFailure() throws Exception {
    // given
    UUID fileId = UUID.randomUUID();
    MockMultipartFile file = createTestFile("force_failure.txt", "fail");

    BinaryContent savedContent = transactionTemplate.execute(status -> {
      BinaryContent content = new BinaryContent(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getSize()
      );
      ReflectionTestUtils.setField(content, "id", fileId);
      return binaryContentRepository.saveAndFlush(content);
    });

    // when
    eventPublisher.publishEvent(new FileUploadEvent(fileId, file));

    // then
    await()
        .atMost(5, TimeUnit.SECONDS)
        .pollInterval(500, TimeUnit.MILLISECONDS)
        .untilAsserted(() -> {
          transactionTemplate.executeWithoutResult(__ -> {
            BinaryContent updated = binaryContentRepository.findById(fileId)
                .orElseThrow();

            entityManager.refresh(updated);

            assertThat(updated.getUploadStatus())
                .isEqualTo(BinaryContentUploadStatus.FAILED);

            // 실패 기록 검증
            List<AsyncTaskFailure> failures = asyncTaskFailureRepository
                .findByRequestId(MDC.get("requestId"));
            assertThat(failures).hasSize(1);
          });
        });
  }

  private MockMultipartFile createTestFile(String filename, String content) {
    return new MockMultipartFile(
        "file",
        filename,
        "text/plain",
        content.getBytes()
    );
  }

  @AfterEach
  void cleanup() {
    // 테스트 간 격리 보장
    transactionTemplate.executeWithoutResult(__ -> {
      binaryContentRepository.deleteAll();
      asyncTaskFailureRepository.deleteAll();
    });
  }
}
