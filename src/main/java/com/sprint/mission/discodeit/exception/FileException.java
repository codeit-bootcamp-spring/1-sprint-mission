package com.sprint.mission.discodeit.exception;

import java.io.IOException;

import static com.sprint.mission.discodeit.constant.FileConstant.FILE_WRITE_ERROR;

public class FileException extends IOException {
  public FileException(){
    super(FILE_WRITE_ERROR);
  }
}
