package com.sprint.mission.discodeit.dto.binaryContentDto;

import org.springframework.web.multipart.MultipartFile;

public record CreateBinaryContentRequestDto (MultipartFile multipartFile, String filePath) {
}
