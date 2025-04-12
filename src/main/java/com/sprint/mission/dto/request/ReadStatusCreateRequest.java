package com.sprint.mission.dto.request;

import com.sprint.mission.entity.addOn.ReadStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
        @NotNull(message = "유저 ID는 필수입니다.")
        UUID userId,
        @NotNull(message = "채널 ID는 필수입니다.")
        UUID channelId,
        @NotNull(message = "마지막 읽은 시간은 필수입니다.")
        Instant lastReadAt) {
//
//    public ReadStatus toEntity() {
//        return new ReadStatus(userId, channelId, lastReadAt);
//    }
}
