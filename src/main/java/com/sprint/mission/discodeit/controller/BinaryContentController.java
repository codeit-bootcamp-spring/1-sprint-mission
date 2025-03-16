package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentApi {

    private final BinaryContentService binaryContentService;

    @GetMapping("/{id}")
    public ResponseEntity<BinaryContentDto> findByIdBinaryContent(@PathVariable UUID id) {
        BinaryContentDto binaryContentResponseDto = binaryContentService.find(id);

        return ResponseEntity.ok(binaryContentResponseDto);
    }


    @GetMapping
    public ResponseEntity<List<BinaryContentDto>> findByIdBinaryContent() {
        List<BinaryContentDto> binaryContents = binaryContentService.findAll();

        return ResponseEntity.ok(binaryContents);
    }
}
