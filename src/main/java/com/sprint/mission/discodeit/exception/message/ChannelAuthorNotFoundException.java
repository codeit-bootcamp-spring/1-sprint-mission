package com.sprint.mission.discodeit.exception.message;

public class ChannelAuthorNotFoundException extends RuntimeException {

  public ChannelAuthorNotFoundException() {
    super();
  }

  public ChannelAuthorNotFoundException(String authorId) {
    super(String.format("Author with is id %s not found", authorId));
  }
}
