package com.sprint.mission.dto.request;

import com.sprint.mission.entity.addOn.BinaryContent;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Data
@NoArgsConstructor
public class BinaryContentDto {
    String fileName;
    String contentType;
    byte[] bytes;

    public BinaryContentDto(String fileName, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    public BinaryContent toEntity() {
        return new BinaryContent(fileName, contentType, bytes);
    }

    public static Optional<BinaryContentDto> fileToBinaryContentDto(MultipartFile file) {
        if (file.isEmpty()) {
            return Optional.empty();
        }
        try {
            BinaryContentDto binaryContentDto = new BinaryContentDto(file.getName(),
                file.getContentType(), file.getBytes());
            return Optional.of(binaryContentDto);
        } catch (IOException e) {
            // 나중에 커스텀 에러처리하기
            throw new RuntimeException(e);
        }
    }
}
