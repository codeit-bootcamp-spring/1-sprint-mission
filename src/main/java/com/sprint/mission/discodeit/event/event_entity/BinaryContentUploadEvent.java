package com.sprint.mission.discodeit.event.event_entity;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class BinaryContentUploadEvent {

  private final List<BinaryContent> contents;
  private final List<MultipartFile> files;
  private final User user;
}
