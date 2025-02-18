package com.sprint.mission.discodeit.exception;

import static com.sprint.mission.discodeit.constant.FileConstant.FILE_WRITE_ERROR;

public class FileException extends RuntimeException {
  public FileException(){
    super(FILE_WRITE_ERROR);
  }
  public FileException(String message){
    super(message);
  }
}
