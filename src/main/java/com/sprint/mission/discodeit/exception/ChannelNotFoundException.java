package com.sprint.mission.discodeit.exception;

import static com.sprint.mission.discodeit.constant.ChannelConstant.CHANNEL_NOT_FOUND;

public class ChannelNotFoundException extends IllegalArgumentException{
  public ChannelNotFoundException(){
    super(CHANNEL_NOT_FOUND);
  }
}
