package com.sprint.mission.discodeit.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public class MessageDTO {

    @Builder
    public record request(UUID author, UUID channel, String content) {
    }

    @Builder
    public record response(Long id, UUID uuid, UUID author, UUID channel, String content) {
    }

    @Builder
    public record update(Long id, String content, List<MultipartFile> attachments) {
    }

    @Builder
    public record idResponse(Long id, UUID uuid) {
    }

}
