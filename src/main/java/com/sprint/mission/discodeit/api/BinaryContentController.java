package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binary-contents")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    public BinaryContentController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }

    // POST: 바이너리 파일 업로드
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BinaryContent> createBinaryContent( BinaryContentCreateRequest request) {
        BinaryContent binaryContent = binaryContentService.create(request);
        return ResponseEntity.status(201).body(binaryContent);
    }

    // GET: 단일 바이너리 파일 조회
    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity.ok(binaryContent);
    }

    // GET: 여러 바이너리 파일 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContent>> getBinaryContents(@RequestParam List<UUID> ids) {
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(ids);
        return ResponseEntity.ok(binaryContents);
    }
}
