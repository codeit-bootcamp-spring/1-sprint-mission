package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChannelCreatePublicDTO {


  @NotBlank(message = "채널 이름은 필수입니다.")
  private String name;
  
  private String description;
}
