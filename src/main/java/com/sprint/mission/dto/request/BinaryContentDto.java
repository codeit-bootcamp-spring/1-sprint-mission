package com.sprint.mission.dto.request;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.BinaryContent;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
public record BinaryContentDto (
    String fileName,
    String contentType,
    byte[] bytes){

    public BinaryContent toEntity() {
        return new BinaryContent(fileName, contentType, bytes);
    }

    public static Optional<BinaryContentDto> fileToBinaryContentDto(MultipartFile file) {
        log.info("file : {}", file);
        if (file == null || file.isEmpty()) {
            return Optional.empty();
        }
        try {
            BinaryContentDto binaryContentDto = new BinaryContentDto(file.getName(),
                file.getContentType(), file.getBytes());
            return Optional.of(binaryContentDto);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
        }
    }
}
