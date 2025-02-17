package com.sprint.mission.discodeit.dto.binaryContentDto;

import org.springframework.web.multipart.MultipartFile;

public record ProfileImageFileDto(MultipartFile profileImageFile, String filePath) {
}
