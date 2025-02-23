package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final BinaryContentService binaryContentService;

    public FileController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }

    // 바이너리 파일 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContent>> getFiles() {
        List<BinaryContent> files = binaryContentService.findAllFiles(null);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(files, headers, HttpStatus.OK);
    }
}
