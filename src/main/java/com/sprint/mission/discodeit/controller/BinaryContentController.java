package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentAPI;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentAPI {
    private final BinaryContentService binaryContentService;

    @GetMapping("/{binaryContentId}") //파일 다운로드
    public ResponseEntity<BinaryContent> findFile(@PathVariable("binaryContentId") UUID id) {
        BinaryContent content = binaryContentService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(content);
    }

    @GetMapping// 여러 파일 조회
    public ResponseEntity<List<BinaryContent>>getFilesByIds(@RequestParam("binaryContentIds") List<UUID> ids) {
        List<BinaryContent> contents = binaryContentService.findAllById(ids);
        return ResponseEntity.status(HttpStatus.OK).body(contents);
    }

}
