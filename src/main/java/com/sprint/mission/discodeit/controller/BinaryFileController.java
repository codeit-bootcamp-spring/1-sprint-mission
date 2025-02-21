package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentFindResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binary-file")
@RequiredArgsConstructor
public class BinaryFileController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentFindResponse> binaryContentFindById(@PathVariable UUID binaryContentId){
        return ResponseEntity.ok(binaryContentService.findBinaryContentById(binaryContentId));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentFindResponse>> binaryContentFindAll(){
        return ResponseEntity.ok(binaryContentService.findAll());
    }
}
