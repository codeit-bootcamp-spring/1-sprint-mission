package com.sprint.mission.discodeit.exception;

import static com.sprint.mission.discodeit.constant.MessageConstant.MESSAGE_NOT_FOUND;

public class MessageNotFoundException extends IllegalArgumentException{
  public MessageNotFoundException(){
    super(MESSAGE_NOT_FOUND);
  }
}
