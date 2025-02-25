package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/{fileId}")
    public BinaryContentResponse getFile(@PathVariable UUID fileId) {
        return binaryContentService.findByIdOrThrow(fileId);
    }

    @GetMapping
    public List<BinaryContentResponse> getFileList(@RequestParam("ids") List<UUID> fileIds) {
        return binaryContentService.findAllByIdIn(fileIds);
    }
}
