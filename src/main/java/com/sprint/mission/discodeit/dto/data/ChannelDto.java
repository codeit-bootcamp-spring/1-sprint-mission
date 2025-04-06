package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
    @NotNull(message = "ID는 필수입니다.")
    UUID id,

    @NotNull(message = "채널타입은 필수입니다.")
    ChannelType type,

    @NotBlank(message = "채널 이름은 비어있을 수 없습니다.")
    @Size(max = 100, message = "채널 이름은 100자 를 초과할 수 없습니다.")
    String name,

    @Size(max = 500, message = "채널 설명은 500자를 초과할 수 없습니다.")
    String description,

    @NotNull(message = "참여자 목록은 필수입니다.")
    @Size(min = 1, message = "채널에는 최소 1명의 참여자가 존재해야 합니다.")
    List<UserDto> participants,

    Instant lastMessageAt
) {

}