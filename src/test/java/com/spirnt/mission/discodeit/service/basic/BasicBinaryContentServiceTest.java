package com.spirnt.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.entity.BinaryContent;
import com.spirnt.mission.discodeit.entity.BinaryContentUploadStatus;
import com.spirnt.mission.discodeit.exception.BinaryContent.FileException;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import com.spirnt.mission.discodeit.storage.BinaryContentStorage;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class BasicBinaryContentServiceTest {

    @Autowired
    BinaryContentRepository binaryContentRepository;

    @Autowired
    BasicBinaryContentService binaryContentService;

    @MockitoBean
    BinaryContentStorage binaryContentStorage;

    @Test
    void 업로드_재시도_실패시_FAILED로_상태변경됨() throws Exception {
        // given
        String fileName = "test.txt";
        byte[] bytes = "fail this upload".getBytes();
        String contentType = "text/plain";

        doReturn(CompletableFuture.failedFuture(new FileException(Map.of())))
            .when(binaryContentStorage)
            .put(any(), any());

        // when
        BinaryContentDto dto = binaryContentService.create(
            new BinaryContentCreateRequest(fileName, contentType, bytes)
        );

        // 비동기 처리를 기다림
        Thread.sleep(3000); // 혹은 awaitility 사용

        // then
        BinaryContent entity = binaryContentRepository.findById(dto.getId()).orElseThrow();
        assertEquals(BinaryContentUploadStatus.FAILED, entity.getUploadStatus());
    }
}