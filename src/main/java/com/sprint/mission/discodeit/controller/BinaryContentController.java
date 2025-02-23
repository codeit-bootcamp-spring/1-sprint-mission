package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.basic.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.UUID;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @PostMapping
    public ResponseEntity<Void> uploadFile(@ModelAttribute BinaryContentRequest binaryContentRequest) {
        BinaryContent content = binaryContentService.create(binaryContentRequest);
        return ResponseEntity.created(URI.create("/files/" + content.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFileById(@PathVariable UUID id) throws MalformedURLException {

        Path path = binaryContentService.find(id);
        UrlResource resource = new UrlResource("file:" + path);

        String encode = UriUtils.encode(path.getFileName().toString(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encode + "\"";

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);
    }
}