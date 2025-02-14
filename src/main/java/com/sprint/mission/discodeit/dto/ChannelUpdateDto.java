package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.util.Optional;

@Getter
public class ChannelUpdateDto {
  @NotNull
  private String channelId;
  @NotBlank
  @Size(min = 2, max = 10)
  private String channelName;
  @Min(1)
  @Max(50)
  private Integer maxNumberOfPeople;

  public ChannelUpdateDto(String channelId, String channelName, Integer maxNumberOfPeople) {
    this.channelId = channelId;
    this.channelName = channelName;
    this.maxNumberOfPeople = maxNumberOfPeople;
  }
}
