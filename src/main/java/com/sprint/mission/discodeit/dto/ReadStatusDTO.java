package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class ReadStatusDTO {

    @Builder
    public record request(UUID userId, UUID channelId, Instant lastReadAt) {
        public static request of(UUID userId, UUID channelId, Instant lastReadAt) {
            return ReadStatusDTO.request.builder()
                    .userId(userId)
                    .channelId(channelId)
                    .lastReadAt(lastReadAt)
                    .build();
        }
    }

    @Builder
    public record response(Long id, UUID uuid, UUID userId, UUID channelId, Instant lastReadAt) {
        public static response from(Map.Entry<Long, ReadStatus> readStatusEntry) {
            ReadStatus readStatus = readStatusEntry.getValue();
            return response.builder()
                    .id(readStatusEntry.getKey())
                    .uuid(readStatus.getId())
                    .userId(readStatus.getUserId())
                    .channelId(readStatus.getChannelId())
                    .lastReadAt(readStatus.getLastReadAt())
                    .build();
        }
    }

    @Builder
    public record update(Long id, Instant lastReadAt) {
        public static update of(Long id, Instant lastReadAt) {
            return update.builder()
                    .id(id)
                    .lastReadAt(lastReadAt)
                    .build();
        }
    }
}
