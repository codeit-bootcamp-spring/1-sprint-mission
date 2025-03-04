package com.sprint.mission.discodeit.dto.request.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Schema(name = "PrivateChannelCreateDTO", description = "비공개 채널 생성 요청 정보를 담은 DTO")
public record PrivateChannelCreateDTO(

    @Schema(description = "채널 소유자 식별자", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "Owner Id is required") UUID ownerId,

    @Schema(description = "채널 구성원 식별자 목록")
    @NotEmpty(message = "memberIds are required") List<UUID> memberIds
) {

}
