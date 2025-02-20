package com.sprint.mission.discodeit.exception;

import static com.sprint.mission.discodeit.constant.ChannelConstant.NOT_CHAT_CHANNEL;

public class NotChatChannelException extends IllegalArgumentException {
  public NotChatChannelException(){
    super(NOT_CHAT_CHANNEL);
  }
}
