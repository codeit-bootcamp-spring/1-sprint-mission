package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChannelUpdateDto {

  @NotBlank
  @Size(min = 2, max = 10)
  private String channelName;
  @Min(1)
  @Max(50)
  private Integer maxNumberOfPeople;

  public ChannelUpdateDto(String channelName, Integer maxNumberOfPeople) {
    this.channelName = channelName;
    this.maxNumberOfPeople = maxNumberOfPeople;
  }
}
