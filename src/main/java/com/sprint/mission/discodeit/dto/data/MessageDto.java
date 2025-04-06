package com.sprint.mission.discodeit.dto.data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
    @NotNull(message = "ID는 필수입니다.")
    UUID id,

    @NotNull(message = "생성일시는 필수입니다.")
    @PastOrPresent(message = "생성 일시는 현재 또는 과거여야 합니다.")
    Instant createdAt,

    @PastOrPresent(message = "생성 일시는 현재 또는 과거여야 합니다.")
    Instant updatedAt,
    @NotBlank(message = "메시지 내용은 비어 있을 수 없습니다.")
    @Size(max = 1000, message = "메시지 내용은 1000자를 초과할 수 없습니다.")
    String content,

    @NotNull(message = "채널 ID는 필수입니다.")
    UUID channelId,

    @NotNull(message = "작성자는 필수입니다.")
    @Valid
    UserDto author,

    @Valid
    List<BinaryContentDto> attachments
) {

}