package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

@Getter
public class Channel extends BaseUpdatableEntity {

  private static final long serialVersionUID = 1L;
  //채널명
  private String channelName;

  //채널 종류 - 음성, 텍스트
  private final ChannelCategory channelCategory;

  //채널 공개 여부
  private ChannelType channelType;

  private String description;


  public Channel(String channelName, ChannelType channelType, ChannelCategory channelCategory,
      String description) {
    this.channelName = channelName;
    this.channelCategory = channelCategory;
    this.channelType = channelType;
    this.description = description;
  }

  public void setChannelName(String channelName) {
    this.channelName = channelName;
  }

  //채널 생성된 이후, 생성 시간을 변경할 수 없으므로 update 미구현

  public void setChannelType(ChannelType channelType) {
    this.channelType = channelType;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isUpdated(UpdateChannelDto updateChannelDto) {
    //변경 여부 체크
    boolean isUpdated = false;

    String newChannelName = updateChannelDto.channelName();
    if (newChannelName != null && !newChannelName.isEmpty() && !newChannelName.equals(
        channelName)) {
      channelName = newChannelName;
      isUpdated = true;
    }

    String newDescription = updateChannelDto.description();
    if (newDescription != null && !newDescription.isEmpty() && !newDescription.equals(
        description)) {
      description = newDescription;
      isUpdated = true;
    }

    return isUpdated;
  }

}
