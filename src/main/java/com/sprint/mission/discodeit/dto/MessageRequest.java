package com.sprint.mission.discodeit.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public record MessageRequest() {
    public record Create(
            String content,
            UUID channelId,
            UUID userId,
            List<MultipartFile> files
    ) {}

    public record Update(
            String content,
            List<MultipartFile> files
    ) {}
}
