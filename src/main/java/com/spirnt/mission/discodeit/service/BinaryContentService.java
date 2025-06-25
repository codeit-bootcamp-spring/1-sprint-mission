package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentDto create(UUID userId, BinaryContentCreateRequest binaryContentCreateRequest);

    BinaryContentDto find(UUID id);


    List<BinaryContentDto> findAllByIdIn(List<UUID> uuids);

}
