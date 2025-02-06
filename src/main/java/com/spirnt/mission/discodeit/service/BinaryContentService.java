package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import com.spirnt.mission.discodeit.enity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    UUID create(BinaryContentCreate binaryContentCreate);
    BinaryContent find(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> uuids);
    void delete(UUID id);
}
