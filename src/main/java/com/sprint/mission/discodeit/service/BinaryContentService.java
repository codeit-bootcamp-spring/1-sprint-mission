package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binary_content.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;

public interface BinaryContentService {
  BinaryContent create(CreateBinaryContentDto dto);
  BinaryContent find(String id);
  List<BinaryContent> findAllByIdIn(List<String> ids);
  void delete(String id);
}
