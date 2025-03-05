package com.sprint.mission.discodeit.exception.message;

public class MessageNotFoundException extends RuntimeException {

  public MessageNotFoundException() {
    super();
  }

  public MessageNotFoundException(String messageId) {
    super(String.format("Message with id %s not found", messageId));
  }
}
