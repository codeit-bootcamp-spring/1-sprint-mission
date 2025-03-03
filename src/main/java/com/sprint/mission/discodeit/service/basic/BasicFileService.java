package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.FileResponse;
import com.sprint.mission.discodeit.entity.FileEntity;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicFileService implements FileService {

    private final FileRepository fileRepository;

    @Value("${file.storage.path:C:/files/}")  // application.properties에서 설정 가능
    private String fileStoragePath;

    // ✅ 파일 1개 다운로드
    @Override
    public Resource downloadFile(UUID fileId) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("❌ 파일을 찾을 수 없습니다: " + fileId));

        try {
            Path filePath = Paths.get(fileStoragePath, file.getFilename()).toAbsolutePath();
            log.info("📂 파일 다운로드 요청: {}", filePath);

            if (!Files.exists(filePath)) {
                log.warn("⚠️ 파일이 존재하지 않음: {}", filePath);
                return null;
            }

            return new UrlResource(filePath.toUri());
        } catch (Exception e) {
            throw new RuntimeException("🚨 파일을 로드할 수 없습니다: " + fileId, e);
        }
    }

    // ✅ 여러 개 파일 정보 조회
    @Override
    public List<FileResponse> getFiles(List<UUID> fileIds) {
        List<FileEntity> files = fileRepository.findByIdIn(fileIds);
        return files.stream()
                .map(file -> new FileResponse(file.getId(), file.getFilename(), file.getFileType()))
                .collect(Collectors.toList());
    }

    // ✅ 파일 삭제 기능 추가
    @Override
    public void deleteFile(UUID fileId) {
        Optional<FileEntity> fileOptional = fileRepository.findById(fileId);
        if (fileOptional.isPresent()) {
            FileEntity file = fileOptional.get();
            Path filePath = Paths.get(fileStoragePath, file.getFilename()).toAbsolutePath();

            try {
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    log.info("✅ 파일 삭제 완료: {}", filePath);
                } else {
                    log.warn("⚠️ 파일이 존재하지 않음: {}", filePath);
                }
            } catch (Exception e) {
                throw new RuntimeException("🚨 파일 삭제 실패: " + fileId, e);
            }

            fileRepository.deleteById(fileId);
            log.info("✅ 파일 DB 레코드 삭제 완료: {}", fileId);
        } else {
            log.warn("❌ 파일 삭제 실패 - 존재하지 않는 파일 ID: {}", fileId);
            throw new IllegalArgumentException("파일을 찾을 수 없습니다.");
        }
    }
}
