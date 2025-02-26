package com.sprint.mission.discodeit.dto.binarycontent;

import org.springframework.web.multipart.MultipartFile;

public record ProfileImageFileDto(MultipartFile profileImageFile, String filePath) {
}
