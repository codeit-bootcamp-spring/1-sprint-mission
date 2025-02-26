package com.sprint.mission.discodeit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BinaryControllerExceptionAdvice {

//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(BinaryContentException.class)
//    public ErrorResult userNotFound(BinaryContentException e) {
//        log.error("Binary exception: {}", e.getMessage());
//
//        return new ErrorResult(ErrorCode.INTERNAL_SERVER_ERROR_BINARY);
//    }
}
