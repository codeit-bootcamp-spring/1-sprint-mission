package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContentDto.FindBinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<FindBinaryContentResponseDto> findByIdBinaryContent(@PathVariable UUID id) {
        FindBinaryContentResponseDto binaryContentResponseDto = binaryContentService.find(id);

        return ResponseEntity.ok(binaryContentResponseDto);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<FindBinaryContentResponseDto>> findByIdBinaryContent() {
        List<FindBinaryContentResponseDto> binaryContents = binaryContentService.findAll();

        return ResponseEntity.ok(binaryContents);
    }
}
