package com.sprint.mission.discodeit.controller.api;

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

    // 프론트 연결 전 수정하기
//    @GetMapping(value ="/{fileId}", produces = {MediaType.IMAGE_JPEG_VALUE})
//    public byte[] getFile(@PathVariable UUID fileId) {
//        return binaryContentService.findByIdOrThrow(fileId).getBytes();
//    }

    @GetMapping("/{fileId}")
    public BinaryContent getFile(@PathVariable UUID fileId) {
        return binaryContentService.findByIdOrThrow(fileId);
    }

    @GetMapping
    public List<BinaryContent> getFileList(@RequestParam("ids") List<UUID> fileIds) {
        return binaryContentService.findAllByIdIn(fileIds);
    }
}
