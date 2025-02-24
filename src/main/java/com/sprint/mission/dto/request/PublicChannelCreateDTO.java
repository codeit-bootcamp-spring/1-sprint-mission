package com.sprint.mission.dto.request;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateDTO(
        @NotBlank(message = "이름은 필수입니다.")
        @Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하로 입력해주세요.")
        String name,
        @NotBlank(message = "설명은 필수입니다.")
    String description
) {
  public Channel toChannel() {
    return new Channel(name, description, ChannelType.PUBLIC);
  }
}
