package com.sprint.mission.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    public static BinaryContentDto filetoBinaryContentDto(MultipartFile file) {
        try {
            return new BinaryContentDto(file.getName(), file.getContentType(), file.getBytes());
        } catch (IOException e) {
            // 나중에 커스텀 에러처리하기
            throw new RuntimeException(e);
        }
    }
}
