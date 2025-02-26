package com.sprint.mission.discodeit.dto.request.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;


@Schema(name = "PublicChannelCreateDTO", description = "공개 채널 생성 요청 정보를 담은 DTO")
public record PublicChannelCreateDTO(
    @Schema(description = "채널 소유자 식별자", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "Owner Id is required") UUID ownerId,

    @Schema(description = "채널 제목", example = "자유 토론")
    @NotEmpty(message = "Title is required") String name,

    @Schema(description = "채널 설명", example = "일반 토론을 위한 채널입니다.")
    @NotEmpty(message = "Description is required") String description
) {

}
