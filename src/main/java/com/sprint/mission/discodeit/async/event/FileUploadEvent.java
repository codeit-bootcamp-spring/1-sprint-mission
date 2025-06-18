package com.sprint.mission.discodeit.async.event;

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
  private MultipartFile file;
  private UUID receiverId;

}
