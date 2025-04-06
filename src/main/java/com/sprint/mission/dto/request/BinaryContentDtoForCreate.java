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
}
