package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service("fileBinaryContentService")
@RequiredArgsConstructor
public class FileBinaryContentService implements BinaryContentService {

    private static final String UPLOAD_DIR = "uploads/";

    @PostConstruct
    public void init() {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @Override
    public void storeBinaryContent(UUID ownerId, byte[] data, String fileType) {
        UUID fileId = UUID.randomUUID();
        String fileName = UPLOAD_DIR + fileId.toString();

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(data);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }
    }

    @Override
    public Optional<BinaryContent> getBinaryContent(UUID id) {
        String filePath = UPLOAD_DIR + id.toString();
        Path path = Paths.get(filePath);

        if (Files.exists(path)) {
            try {
                byte[] data = Files.readAllBytes(path);
                return Optional.of(new BinaryContent(id, null, data, "unknown"));
            } catch (IOException e) {
                throw new RuntimeException("파일 읽기 오류", e);
            }
        }
        return Optional.empty();
    }

    // ✅ 추가: 파일 삭제 기능 구현
    @Override
    public boolean deleteBinaryContent(UUID id) {
        String filePath = UPLOAD_DIR + id.toString();
        File file = new File(filePath);
        return file.exists() && file.delete();
    }
}
