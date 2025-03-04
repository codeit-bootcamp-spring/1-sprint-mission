package com.sprint.mission.discodeit.dto.request.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(name = "ChannelUpdateDTO", description = "채널 업데이트 요청 정보를 담은 DTO")
public record ChannelUpdateDTO(
    @Schema(description = "채널 식별자", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "Channel Id is required") UUID channelId,

    @Schema(description = "새 채널 제목", example = "업데이트된 채널명")
    @NotEmpty(message = "New Title is required") String newName,

    @Schema(description = "새 채널 설명", example = "업데이트된 채널 설명")
    @NotEmpty(message = "New Description is required") String newDescription
) {

}
