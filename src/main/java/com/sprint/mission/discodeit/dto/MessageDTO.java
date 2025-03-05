package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MessageDTO {

    @Builder
    public record request(UUID author, UUID channel, String content) {
    }

    @Builder
    public record response(Long id, UUID uuid, UUID author, UUID channel, String content, Instant createdAt) {
        public static response from(Map.Entry<Long, Message> messageEntry) {
            Message message = messageEntry.getValue();
            return response.builder()
                    .id(messageEntry.getKey())
                    .uuid(message.getId())
                    .author(message.getAuthorId())
                    .channel(message.getChannelId())
                    .content(message.getContent())
                    .createdAt(message.getCreatedAt())
                    .build();
        }
    }

    @Builder
    public record update(Long id, String content, List<MultipartFile> attachments) {
        public static update of(Long id, String content, List<MultipartFile> attachments) {
            return update.builder()
                    .id(id)
                    .content(content)
                    .attachments(attachments)
                    .build();
        }
    }
}
