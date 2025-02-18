package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
  BinaryContent createBinaryContent(BinaryContent paramBinaryContent);

  BinaryContent findBinaryContentById(UUID paramUUID);

  List<BinaryContent> findAllFiles(List<UUID> paramList);

  void deleteFile(UUID paramUUID);
}
