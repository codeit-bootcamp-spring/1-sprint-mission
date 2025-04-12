package com.sprint.mission.dto.request;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateDTO(

        @Schema(example = "코드잇 채널")
        @NotBlank(message = "이름은 필수입니다.")
        @Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하로 입력해주세요.")
        String name,

        @Schema(example = "Spring 교육과정입니다.")
        @NotBlank(message = "설명은 필수입니다.")
        String description) {
}
