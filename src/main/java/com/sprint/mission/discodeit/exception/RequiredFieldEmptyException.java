package com.sprint.mission.discodeit.exception;

import static com.sprint.mission.discodeit.constant.ErrorConstant.REQUIRED_FIELD_EMPTY;

public class RequiredFieldEmptyException extends IllegalArgumentException {
  public RequiredFieldEmptyException(){
    super();
  }

  public RequiredFieldEmptyException(String message){
    super(message + REQUIRED_FIELD_EMPTY);
  }
}
