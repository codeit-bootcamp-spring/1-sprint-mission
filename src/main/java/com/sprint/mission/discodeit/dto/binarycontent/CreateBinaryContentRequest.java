package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
UUID id;
String fileName;
String filePath;
Long fileSize;
String mimeType;
BinaryContentType type;
Instant createAt;
*/
@Getter
@AllArgsConstructor
public class CreateBinaryContentRequest {
    @NotBlank(message = "파일명은 필수 항목입니다.")
    private String fileName;

    @NotNull(message = "파일 유형은 필수 항목입니다.")
    private BinaryContentType type;

    private String fileExtension;

    @Positive
    private Long fileSize;

    @NotBlank(message = "MIME 타입은 필수 항목입니다.")
    private String mimeType;
}
