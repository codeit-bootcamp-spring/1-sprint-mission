package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentMapper binaryContentMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Operation(summary = "조회", description = "단건 조회")
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable("binaryContentId") UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);

        if (binaryContent == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(binaryContent);
    }

    @Operation(summary = "조회", description = "전부 조회")
    @GetMapping
    public ResponseEntity<List<BinaryContent>> getBinaryContents(
            @RequestParam("ids") List<UUID> binaryContentIds) {

        if (binaryContentIds == null || binaryContentIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
        return ResponseEntity.ok(binaryContents);
    }

    @GetMapping({"/download"})
    public ResponseEntity<?> downloadContent(
            @RequestParam("binaryContentId") UUID binaryContentId) throws IOException {
        try {
            BinaryContent binaryContent = binaryContentService.find(binaryContentId);
            BinaryContentDto dto = binaryContentMapper.toDto(binaryContent);
            return binaryContentStorage.download(dto);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("바이너리 콘텐츠", "id", binaryContentId);
        }
    }
}