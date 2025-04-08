package com.sprint.mission.discodeit.dto.binaryContent;


import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BinaryContentDto {

  private UUID id;
  private String fileName;
  private long size;
  private String contentType;
}
