package com.sprint.mission.discodeit.async.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUploadEvent {

  private UUID fileId;
  @JsonIgnore
  private MultipartFile file;
  private UUID receiverId;

}
