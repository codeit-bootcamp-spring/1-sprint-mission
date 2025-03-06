package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binarycontent.FindBinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentApi {

    private final BinaryContentService binaryContentService;

    @GetMapping("/{id}")
    public ResponseEntity<FindBinaryContentResponseDto> findByIdBinaryContent(@PathVariable UUID id) {
        FindBinaryContentResponseDto binaryContentResponseDto = binaryContentService.find(id);

        return ResponseEntity.ok(binaryContentResponseDto);
    }


    @GetMapping
    public ResponseEntity<List<FindBinaryContentResponseDto>> findByIdBinaryContent() {
        List<FindBinaryContentResponseDto> binaryContents = binaryContentService.findAll();

        return ResponseEntity.ok(binaryContents);
    }
}
