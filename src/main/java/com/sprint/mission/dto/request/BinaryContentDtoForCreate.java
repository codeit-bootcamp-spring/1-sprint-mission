package com.sprint.mission.dto.request;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.BinaryContent;

import java.util.Optional;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
public record BinaryContentDtoForCreate(

        @Schema(example = "zessy")
        String fileName,
        String contentType,
        Long size,
        byte[] bytes) {

    public BinaryContent toEntity() {
        return new BinaryContent(fileName, contentType, size);
    }
//
//    public static Optional<BinaryContentDtoForCreate> convertToBinaryContentDto(MultipartFile file) {
//        log.info("file : {}", file);
//        if (file == null || file.isEmpty()) {
//            return Optional.empty();
//        }
//        try {
//            BinaryContentDtoForCreate binaryContentDtoForCreate = new BinaryContentDtoForCreate(file.getName(),
//                    file.getContentType(), file.getSize(), file.getBytes());
//            return Optional.of(binaryContentDtoForCreate);
//        } catch (IOException e) {
//            throw new CustomException(ErrorCode.FILE_CONVERT_ERROR);
//        }
//    }
}
