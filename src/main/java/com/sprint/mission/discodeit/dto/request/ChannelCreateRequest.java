package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record ChannelCreateRequest(
    @NotBlank(message = "채널 이름은 필수입니다.")
    @Size(min = 2, max = 50, message = "채널 이름은 2자 이상 50자 이하여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣_-]*$", message = "채널 이름은 영문, 한글, 숫자, 언더스코어(_), 하이픈(-)만 사용할 수 있습니다.")
    String name,

    @NotNull(message = "생성자 ID는 필수입니다.")
    UUID creatorId
) {} 