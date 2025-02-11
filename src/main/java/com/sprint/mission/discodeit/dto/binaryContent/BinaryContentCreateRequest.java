package com.sprint.mission.discodeit.dto.binaryContent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BinaryContentCreateRequest {
    private UUID userId;
    private UUID messageId;
    private MultipartFile file;
}
