package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Getter
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "file_name")
    private final String fileName;      // 파일 이름

    @Column(name = "size")
    private final Long size;            // 파일 크기(바이트)

    @Column(name = "content_type")
    private final String contentType;   // MIME 타입 (image/png, application/pdf 등)

    @Column(name = "file_path")
    private final String filePath;      // 파일 경로

    // 생성자 호출 시 파일 저장 및 객체 초기화
    public BinaryContent(MultipartFile file, String filePath) throws IOException {

        validationFile(file);
        filePath = validationFilePath(filePath);

        CreateBinaryContentResponseDto createBinaryContentResponseDto = saveFile(file, filePath);

        // 객체 초기화
        this.fileName = createBinaryContentResponseDto.fileName();
        this.contentType = file.getContentType();
        this.size = file.getSize();
        this.filePath = filePath;
    }

    private void validationFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }
    }

    private String validationFilePath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("파일 경로를 입력해주세요.");
        }

        return filePath.trim();
    }

    private CreateBinaryContentResponseDto saveFile(MultipartFile file, String filePath) throws IOException {
        // 저장 경로 설정
        File dir = new File(filePath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 저장할 파일명 생성
        UUID id = UUID.randomUUID();
        String uniqueFileName = id + "_" + file.getOriginalFilename();
        File destinationFile = new File(filePath, uniqueFileName);

        // 파일 저장
        file.transferTo(destinationFile);

        return new CreateBinaryContentResponseDto(id, uniqueFileName);
    }

    public void deleteFile() {
        Path path = Paths.get(filePath, fileName);

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new RuntimeException("파일 삭제 실패: " + e);
            }
        } else {
            System.out.println("삭제할 파일이 존재하지 않음: " + path);
        }
    }
}