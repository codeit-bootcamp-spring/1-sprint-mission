package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NotNull
public class BinaryContentDto {

  private String key;
  private UUID id;
  private String fileName;
  private Long size;
  private String contentType;
}
