package com.sprint.mission.discodeit.dto.message;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public record CreateMessageRequestDto(UUID channelId, UUID writerId , String context, List<MultipartFile> images) {
}
