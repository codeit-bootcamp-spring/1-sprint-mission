package com.sprint.mission.discodeit.exception.channel;

public class PrivateChannelCanNotModifyException extends RuntimeException {

  public PrivateChannelCanNotModifyException() {
    super("Private channel cannot be updated");
  }

  public PrivateChannelCanNotModifyException(String message) {
    super(message);
  }
}
