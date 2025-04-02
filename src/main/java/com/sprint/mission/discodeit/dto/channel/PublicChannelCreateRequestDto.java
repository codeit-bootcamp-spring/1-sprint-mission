package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter
public class PublicChannelCreateRequestDto {

  @NotBlank(message = "채널 이름은 필수")
  @Size(min = 1, max = 20, message = "채널 이름은 한 글자이상 20자 이하")
  private final String name;
  private final String description;

  public PublicChannelCreateRequestDto(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
