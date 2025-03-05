package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;

import java.util.Map;
import java.util.UUID;

public class BinaryContentDTO {

    @Builder
    public record request(byte[] file, UUID referenceId, String mimeType, String filename,
                          BinaryContent.ContentType contentType) {
        public static request of(byte[] file, UUID referenceId, String mimeType, String filename, BinaryContent.ContentType contentType) {
            return request.builder()
                    .file(file)
                    .referenceId(referenceId)
                    .mimeType(mimeType)
                    .filename(filename)
                    .contentType(contentType)
                    .build();
        }
    }

    @Builder
    public record response(Long id, UUID uuid, byte[] file, UUID referenceId, BinaryContent.ContentType contentType) {
        public static response from(Map.Entry<Long, BinaryContent> content) {
            return response.builder()
                    .id(content.getKey())
                    .uuid(content.getValue().getId())
                    .file(content.getValue().getData())
                    .referenceId(content.getValue().getReferenceId())
                    .contentType(content.getValue().getContentType())
                    .build();
        }
    }

    @Builder
    public record convert(Long id, UUID uuid, byte[] file, String mimeType, String filename) {
        public static convert from(Map.Entry<Long, BinaryContent> content) {
            return convert.builder()
                    .id(content.getKey())
                    .uuid(content.getValue().getId())
                    .file(content.getValue().getData())
                    .mimeType(content.getValue().getMimeType())
                    .filename(content.getValue().getFilename())
                    .build();
        }
    }

}
