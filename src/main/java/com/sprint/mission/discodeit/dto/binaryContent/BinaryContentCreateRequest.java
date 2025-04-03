package com.sprint.mission.discodeit.dto.binaryContent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BinaryContentCreateRequest {

  private String fileName;
  private String contentType;
  private byte[] bytes;
}