package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@AllArgsConstructor
public class BinaryContentController {

    private final BinaryContentStorage storage;
    private final BinaryContentService service;

//    @PostMapping("/upload")
//    public ResponseEntity<UUID> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
//        UUID fileId = UUID.randomUUID();
//        storage.put(fileId, file.getBytes());
//        return ResponseEntity.ok(fileId);
//    }

    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> downloadFile(@PathVariable UUID binaryContentId) throws IOException {

//        InputStream byteData = storage.get(binaryContentId);

//        BinaryContentDto byteD = service.findById(binaryContentId);

        BinaryContentDto dto = new BinaryContentDto(binaryContentId, "filename.jpg", 1024L, "image/jpeg"); // 예제 데이터
        return storage.download(dto);
    }
}