package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binary-content")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getBinaryContent(@PathVariable UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        if(binaryContent == null) {
            throw new NoSuchElementException("BinaryContent with id " + binaryContentId + "not found");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + binaryContent.getFileName() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, binaryContent.getContentType());

        return new ResponseEntity<>(binaryContent.getBytes(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, params = "ids")
    public ResponseEntity<List<ResponseEntity<byte[]>>> getMultipleBinaryContents(@RequestParam List<UUID> ids) {
        List<ResponseEntity<byte[]>> responses = binaryContentService.findAllByIdIn(ids).stream()
                .map(binaryContent -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + binaryContent.getFileName() + "\"");
                    headers.add(HttpHeaders.CONTENT_TYPE, binaryContent.getContentType());

                    return new ResponseEntity<>(binaryContent.getBytes(), headers, HttpStatus.OK);
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
