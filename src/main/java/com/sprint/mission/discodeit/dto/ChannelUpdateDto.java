package com.sprint.mission.discodeit.dto;

import java.util.Optional;

public class ChannelUpdateDto {
  private Optional<String> channelName = Optional.empty();
  private Optional<Integer> maxNumberOfPeople = Optional.empty();
  private Optional<String> tag = Optional.empty();
  private Optional<Boolean> isPrivate = Optional.empty();

  public ChannelUpdateDto(Optional<String> channelName,
                          Optional<Integer> maxNumberOfPeople,
                          Optional<String> tag,
                          Optional<Boolean> isPrivate) {
    this.channelName = channelName;
    this.maxNumberOfPeople = maxNumberOfPeople;
    this.tag = tag;
    this.isPrivate = isPrivate;
  }

  public Optional<String> getChannelName() {
    return channelName;
  }

  public Optional<Integer> getMaxNumberOfPeople() {
    return maxNumberOfPeople;
  }

  public Optional<String> getTag() {
    return tag;
  }

  public Optional<Boolean> getIsPrivate() {
    return isPrivate;
  }
}
