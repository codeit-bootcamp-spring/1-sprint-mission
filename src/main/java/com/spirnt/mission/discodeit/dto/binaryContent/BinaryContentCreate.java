package com.spirnt.mission.discodeit.dto.binaryContent;

import org.springframework.web.multipart.MultipartFile;

public record BinaryContentCreate(
        MultipartFile file
) {
}
