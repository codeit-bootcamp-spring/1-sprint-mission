package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.docs.BinaryContentControllerDocs;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentControllerDocs {
    private final BinaryContentService binaryContentService;

    @GetMapping("/{fileId}/download")
    public ResponseEntity<?> download(@PathVariable UUID fileId) {
        return binaryContentService.download(fileId);
    }

//    @GetMapping("/profile")
//    public ResponseEntity<byte[]> getProfileByUserId(@RequestParam UUID userId) throws IOException {
//        return binaryContentService.getProfileImageAsResponse(userId);
//    }
//
//    @GetMapping(path = "/message", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public ResponseEntity<byte[]> getContentsByMessageId(@RequestParam UUID messageId) throws IOException {
//        return binaryContentService.getMessageImageAsResponse(messageId);
//    }
}
