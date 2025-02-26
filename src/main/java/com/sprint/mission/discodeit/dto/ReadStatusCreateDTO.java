package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatusCreateDTO {
    private UUID userId;
    private UUID messageId;
    private Instant readAt;

    // ✅ Lombok이 정상적으로 작동하지 않을 경우 직접 getter 추가
    public Instant getReadAt() {
        return readAt;
    }
}
