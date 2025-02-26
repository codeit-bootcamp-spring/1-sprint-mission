package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    private boolean isImage(String fileType) {
        return fileType.startsWith("image/");
    }

    // 단건 조회 및 다운로드
    @RequestMapping(value = "/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<?> getFile(@PathVariable UUID fileId) {
        BinaryContent binaryContent = binaryContentService.find(fileId);
        Resource resource = new FileSystemResource(Paths.get(binaryContent.getFilePath()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, binaryContent.getFileType())
                // 이미지인 경우 inline으로 바로 보이게, 아닌 경우 다운로드 가능한 첨부파일 형태로
                .header(HttpHeaders.CONTENT_DISPOSITION, isImage(binaryContent.getFileType())
                        ? "inline"
                        : "attachment; filename=\"" + binaryContent.getFileName() + "\"")
                .body(resource);
    }

    // 삭제
    @RequestMapping(value = "/{fileId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteFile(@PathVariable UUID fileId) {
        binaryContentService.delete(fileId);
        return ResponseEntity.noContent().build();
    }

}
