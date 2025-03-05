package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class UserStatusDTO {

    @Builder
    public record request(UUID userId, Instant accessedAt) {
        public static request of(UUID userId, Instant accessedAt) {
            return request.builder()
                    .userId(userId)
                    .accessedAt(accessedAt)
                    .build();
        }
    }

    @Builder
    public record response(Long id, UUID uuid, UUID userId, Instant accessedAt) {
        public static response from(Map.Entry<Long, UserStatus> userStatusEntry) {
            UserStatus userStatus = userStatusEntry.getValue();
            return response.builder()
                    .id(userStatusEntry.getKey())
                    .uuid(userStatus.getId())
                    .userId(userStatus.getUserId())
                    .accessedAt(userStatus.getAccessedAt())
                    .build();
        }
    }

    @Builder
    public record update(Long id, UUID userId, Instant accessedAt) {
        public static update of(Long id, UUID userId, Instant accessedAt) {
            return update.builder()
                    .id(id)
                    .userId(userId)
                    .accessedAt(accessedAt)
                    .build();
        }
    }
}
