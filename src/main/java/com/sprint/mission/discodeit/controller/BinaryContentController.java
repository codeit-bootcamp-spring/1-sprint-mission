package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    // 바이너리 파일 한 개 조회
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentDto> searchBinaryContentByContentId(@PathVariable UUID binaryContentId) {
        BinaryContentDto binaryContentDto = binaryContentService.findById(binaryContentId);
        return ResponseEntity.ok(binaryContentDto);
    }

    // 바이너리 파일 여러 개 조회
    @GetMapping
    public ResponseEntity<List<BinaryContentDto>> searchAllBinaryContents(@RequestParam List<UUID> binaryContentIds){
        List<BinaryContentDto> binaryContentDtoList = binaryContentService.findAllByIdIn(binaryContentIds);
        return ResponseEntity.ok(binaryContentDtoList);
    }
}
