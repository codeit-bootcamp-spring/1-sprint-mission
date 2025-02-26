package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.auth.AuthException;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentException;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.message.MessageException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusException;
import com.sprint.mission.discodeit.exception.user.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AuthException.class)
  public ResponseEntity<String> handleAuthException(AuthException exception) {
    return ResponseEntity.status(exception.getStatus()).body(exception.getMessage());
  }

  @ExceptionHandler(BinaryContentException.class)
  public ResponseEntity<String> handleBinaryContentException(BinaryContentException exception) {
    return ResponseEntity.status(exception.getStatus()).body(exception.getMessage());
  }

  @ExceptionHandler(ChannelException.class)
  public ResponseEntity<String> handleChannelException(ChannelException exception) {
    return ResponseEntity.status(exception.getStatus()).body(exception.getMessage());
  }

  @ExceptionHandler(MessageException.class)
  public ResponseEntity<String> handleMessageException(MessageException exception) {
    return ResponseEntity.status(exception.getStatus()).body(exception.getMessage());
  }

  @ExceptionHandler(ReadStatusException.class)
  public ResponseEntity<String> handleReadStatusException(ReadStatusException exception) {
    return ResponseEntity.status(exception.getStatus()).body(exception.getMessage());
  }

  @ExceptionHandler(UserException.class)
  public ResponseEntity<String> handleUserException(UserException exception) {
    return ResponseEntity.status(exception.getStatus()).body(exception.getMessage());
  }
}
