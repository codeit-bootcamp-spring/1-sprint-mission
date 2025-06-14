package com.sprint.mission.discodeit.service.basic;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContent.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.binarycontent.BinaryContentOperationException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class BasicBinaryContentServiceTest {

    @Autowired
    private BinaryContentService binaryContentService;

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @MockitoBean
    private BinaryContentStorage binaryContentStorage;

    @Test
    void 파일_업로드_실패_시_uploadStatus_FAILED() {
        // given
        MockMultipartFile mockFile = new MockMultipartFile(
            "file", "test.jpg", MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );

        CompletableFuture<Void> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(
            new BinaryContentOperationException(ErrorCode.BINARY_SAVE_FAILED));
        when(binaryContentStorage.put(any(UUID.class), any(byte[].class)))
            .thenReturn(failedFuture);

        // when
        BinaryContent result = binaryContentService.save(mockFile);

        // then - await()를 사용해서 handle 비동기 작업 완료 대기하기 -> 실행 환경이 느린 경우 타임아웃 시간 넉넉히
        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            BinaryContent updated = binaryContentRepository.findById(result.getId()).orElse(null);
            Assertions.assertNotNull(updated);
            Assertions.assertEquals(BinaryContentUploadStatus.FAILED, updated.getUploadStatus());
        });
    }
}