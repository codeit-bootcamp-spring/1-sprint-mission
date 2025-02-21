package com.example.mission.discodeit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileService {
    private static final String UPLOAD_DIR = "uploads/";

    /**
     * 파일 저장 메서드 (업로드)
     */
    public String storeFile(org.springframework.web.multipart.MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();

        // 디렉토리가 없으면 생성
        java.nio.file.Files.createDirectories(filePath.getParent());

        // 파일 저장
        java.nio.file.Files.write(filePath, file.getBytes());

        return fileName; // 저장된 파일 이름 반환
    }

    /**
     * 파일 다운로드 메서드
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found: " + fileName);
            }

            return resource;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading file: " + fileName, e);
        }
    }
}
