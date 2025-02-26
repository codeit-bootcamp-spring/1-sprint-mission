package com.sprint.mission.discodeit.dto.response.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(name = "ChannelResponseDTO", description = "채널 응답 정보를 담은 DTO")
public record ChannelResponseDTO(
    @Schema(description = "채널 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "Channel Id is required") UUID channelId,

    @Schema(description = "채널 이름", example = "자유 토론")
    @NotBlank(message = "Channel Name is required") String name,

    @Schema(description = "채널 설명", example = "일반 토론을 위한 채널입니다.")
    @NotBlank(message = "Channel description is required") String description,

    @Schema(description = "채널 타입", example = "PUBLIC")
    @NotNull(message = "Channel Type is required") ChannelType type,

    @Schema(description = "채널 구성원 ID 목록")
    @NotEmpty(message = "MemberIds is required") List<UUID> memberIds,

    @Schema(description = "최근 메시지 시간", example = "2021-09-01T12:00:00Z")
    Instant lastMessageAt
) {

}
