package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContent create(BinaryContentCreateRequest binaryContentCreateRequest);

  BinaryContent find(UUID id);

  List<BinaryContent> findAllByIdIn(List<UUID> uuids);

  void delete(UUID id);

}
