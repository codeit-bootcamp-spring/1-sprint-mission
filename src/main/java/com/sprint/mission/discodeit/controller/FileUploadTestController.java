package com.sprint.mission.discodeit.controller;

import static com.sprint.mission.discodeit.security.SecurityUtil.getCurrentUserId;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.micrometer.core.annotation.Timed;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/upload")
public class FileUploadTestController {

    private final BinaryContentStorage storage;

    @Timed("upload.sync")
    @PostMapping("/sync")
    public ResponseEntity<String> syncUpload(@RequestBody byte[] data) throws IOException {
        storage.put(UUID.randomUUID(), data);
        return ResponseEntity.ok("동기 업로드 완료");
    }

    @Timed("upload.async")
    @PostMapping("/async")
    public ResponseEntity<String> asyncUpload(@RequestBody byte[] data) {
        UUID userId = getCurrentUserId();
        storage.putAsync(UUID.randomUUID(), data,
                userId,
                status -> {
                });
        return ResponseEntity.ok("비동기 업로드 완료");
    }
}
