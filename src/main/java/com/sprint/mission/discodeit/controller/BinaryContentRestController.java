package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/file")
public class BinaryContentRestController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadProfileImage(@PathVariable UUID fileId) {
        BinaryContent profileImage = binaryContentService.findById(fileId);

        if (profileImage == null || profileImage.getContentType() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(profileImage.getContentType()))  // 파일 타입 지정
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + profileImage.getOriginalFilename() + "\"") // 파일 다운로드
                .body(profileImage.getBytes());
    }
}
