package com.sprint.mission.discodeit.controller.BinaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binary-content")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    public BinaryContentController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(binaryContentService.find(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<List<BinaryContent>> findAll(
            @RequestBody List<UUID> uuids
    ) {
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(uuids));
    }
}
