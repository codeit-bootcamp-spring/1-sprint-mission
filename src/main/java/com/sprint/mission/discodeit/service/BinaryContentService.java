package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binary_content.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BinaryContentService {
  BinaryContent create(CreateBinaryContentDto dto);
  BinaryContent find(String id);
  List<BinaryContent> findAllByIdIn(List<String> ids);
  Map<String, BinaryContent> mapUserToBinaryContent(Set<String> userIds);
  void delete(String id);
}
