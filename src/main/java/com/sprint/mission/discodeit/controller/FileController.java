package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("binary-content")
@RequiredArgsConstructor
public class FileController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET, value = "/{binaryContentId}/download")
    public ResponseEntity<byte[]> downloadSingle(@PathVariable UUID binaryContentId) {

            BinaryContent binaryContent = binaryContentService.find(binaryContentId);
            String fileName = binaryContent.getFileName();
            String contentType = binaryContent.getContentType();

            // 3. 확장자 추가: contentType에서 확장자를 추출하여 파일명에 추가
            String fileExtension = getExtensionFromContentType(contentType);
            String fullFileName = fileName + fileExtension; // 예: "강아지.jpg"

            String encodedFileName = URLEncoder.encode(fullFileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

            // 5. 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, contentType); // 이미 주어진 contentType 사용

            // 6. 파일 데이터 반환
            return new ResponseEntity<>(binaryContent.getBytes(), headers, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/download-multiple")
    public ResponseEntity<byte[]> downloadMultiple(@RequestParam List<UUID> ids) throws IOException {
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(ids);

        // ByteArrayOutputStream 과 ZipOutputStream 으로 압축된 데이터를 생성합니다.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            for (BinaryContent binaryContent : binaryContents) {
                String fileName = binaryContent.getFileName();
                String contentType = binaryContent.getContentType();

                String fileExtension = getExtensionFromContentType(contentType);
                String fullFileName = fileName + fileExtension;

                // ZIP 엔트리 추가 (압축된 파일 이름 설정)
                ZipEntry zipEntry = new ZipEntry(fullFileName);
                zipOutputStream.putNextEntry(zipEntry); // ZIP 파일에 파일을 추가
                zipOutputStream.write(binaryContent.getBytes()); // 바이너리 데이터 기록
                zipOutputStream.closeEntry(); // 엔트리 종료
            }
        }

        // HTTP 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=files.zip"); // 다운로드될 ZIP 파일 이름
        headers.add(HttpHeaders.CONTENT_TYPE, "application/zip"); // ZIP 파일 타입

        // ZIP 파일을 바이트 배열로 반환
        return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
    }

    // contentType 에 맞는 확장자 추출
    private String getExtensionFromContentType(String contentType) {
        switch (contentType) {
            case "image/jpg":
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            default:
                return "";
        }
    }
}
