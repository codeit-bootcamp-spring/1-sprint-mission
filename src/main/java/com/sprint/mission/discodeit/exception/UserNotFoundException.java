package com.sprint.mission.discodeit.exception;

import static com.sprint.mission.discodeit.constant.UserConstant.NO_MATCHING_USER;

public class UserNotFoundException extends NotFoundException {
  public UserNotFoundException(){
    super(NO_MATCHING_USER);
  }
}
