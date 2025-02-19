package com.sprint.mission.discodeit.dto.binary;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
public class BinaryContentCreateRequestDto {
    private UUID userId;
    private UUID messageId;
    private MultipartFile multipartFile;

    public BinaryContentCreateRequestDto(UUID userId, UUID messageId, MultipartFile multipartFile)  {
        this.userId = userId;
        this.messageId = messageId;
        this.multipartFile = multipartFile;
    }
}
