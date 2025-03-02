package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentResponse> findById(
            @RequestParam("binarycontentid") UUID binaryContentId
    ) {
        BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
        if (binaryContent == null) {
            throw new NoSuchElementException("바이너리 콘텐츠를 찾을 수 없습니다: " + binaryContentId);
        }
        BinaryContentResponse response = BinaryContentResponse.from(binaryContent);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentResponse>> findAllByIdIn(
            @RequestParam("binaryContentIds") List<UUID> binaryContentIds
    ) {
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);

        List<BinaryContentResponse> responses = new ArrayList<>();
        for (BinaryContent content : binaryContents) {
            responses.add(BinaryContentResponse.from(content));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }
}