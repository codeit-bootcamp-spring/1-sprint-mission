package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import io.micrometer.core.annotation.Timed;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/performance-test")
public class PerformanceTestController {

    private final BasicBinaryContentService binaryContentService;

    @PostMapping(path = "/sync-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Timed(value = "performance.test.sync.upload", description = "동기 파일 업로드 성능 측정")
    public ResponseEntity<BinaryContentDto> syncUpload(
        @RequestPart("file") MultipartFile file
    ) throws IOException {
        log.info("동기 업로드 테스트 시작: fileName={}, size={}",
            file.getOriginalFilename(), file.getSize());

        BinaryContentCreateRequest request = new BinaryContentCreateRequest(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getBytes()
        );

        long startTime = System.currentTimeMillis();
        BinaryContentDto result = binaryContentService.createSync(request);
        long endTime = System.currentTimeMillis();

        log.info("동기 업로드 완료: 소요시간={}ms", endTime - startTime);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping(path = "/async-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Timed(value = "performance.test.async.upload", description = "비동기 파일 업로드 성능 측정")
    public ResponseEntity<BinaryContentDto> asyncUpload(
        @RequestPart("file") MultipartFile file
    ) throws IOException {
        log.info("비동기 업로드 테스트 시작: fileName={}, size={}",
            file.getOriginalFilename(), file.getSize());

        BinaryContentCreateRequest request = new BinaryContentCreateRequest(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getBytes()
        );

        long startTime = System.currentTimeMillis();
        BinaryContentDto result = binaryContentService.create(request); // 비동기
        long endTime = System.currentTimeMillis();

        log.info("비동기 업로드 응답 완료: 소요시간={}ms", endTime - startTime);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}