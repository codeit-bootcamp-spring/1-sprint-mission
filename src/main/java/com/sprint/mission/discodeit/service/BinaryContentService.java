package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  //BinaryContentDto create(BinaryContentCreateRequest dto);

  BinaryContentDto find(UUID id);

  //List<BinaryContentDto> findAll();

  List<BinaryContentDto> findAllByIdIn(List<UUID> ids);

  //void delete(UUID id);

}
