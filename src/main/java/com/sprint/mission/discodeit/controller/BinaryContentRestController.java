package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/files")
@Tag(name = "Binary Content API" , description = "바이너리 컨텐츠 관리 API")
public class BinaryContentRestController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadProfileImage(@PathVariable UUID id) {
        BinaryContent profileImage = binaryContentService.findById(id);

        if (profileImage == null || profileImage.getContentType() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(profileImage.getContentType()))  // 파일 타입 지정
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + profileImage.getOriginalFilename() + "\"") // 파일 다운로드
                .body(profileImage.getBytes());
    }
}
