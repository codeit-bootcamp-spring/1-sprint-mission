package discodeit.controller;

import discodeit.entity.BinaryContent;
import discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable("id") UUID contentId) {
        return ResponseEntity.ok(binaryContentService.find(contentId));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContent>> getAllBinaryContent(@RequestBody List<UUID> contentIds) {
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(contentIds));
    }
}
