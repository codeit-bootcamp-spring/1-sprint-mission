package com.sprint.mission.discodeit.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinaryContentDto {

  UUID id;
  String fileName;
  Long size;
  String contentType;
  byte[] bytes;
}
