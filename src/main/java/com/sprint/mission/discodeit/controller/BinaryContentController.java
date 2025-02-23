package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.BinaryContentListResponse;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentResponse> find(
            @PathVariable UUID binaryContentId
    ){
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        BinaryContentResponse response = BinaryContentResponse.from(binaryContent);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/batch")
    public ResponseEntity<BinaryContentListResponse> batchDownload(
            @RequestParam List<UUID> ids
    ) {
        List<BinaryContent> contents = binaryContentService.findAllByIdIn(ids);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BinaryContentListResponse.from(contents));
    }
}
