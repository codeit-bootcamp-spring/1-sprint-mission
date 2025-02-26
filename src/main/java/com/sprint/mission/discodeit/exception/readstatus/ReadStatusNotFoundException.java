package com.sprint.mission.discodeit.exception.readstatus;

import java.util.UUID;

public class ReadStatusNotFoundException extends RuntimeException {

  public ReadStatusNotFoundException() {
    super();
  }

  public ReadStatusNotFoundException(UUID readStatusId) {
    super(String.format("ReadStatus with id %s not found", readStatusId));
  }
}
