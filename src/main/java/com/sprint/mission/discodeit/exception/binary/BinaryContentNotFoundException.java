package com.sprint.mission.discodeit.exception.binary;

public class BinaryContentNotFoundException extends RuntimeException {

  public BinaryContentNotFoundException() {
    super();
  }

  public BinaryContentNotFoundException(String binaryContentId) {
    super(String.format("BinaryContent with id %s not found", binaryContentId));
  }
}
