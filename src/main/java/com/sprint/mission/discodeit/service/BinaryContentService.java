package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {

  BinaryContentDto create(UUID contentId, MultipartFile file);

  BinaryContentDto find(UUID contentId);

  List<BinaryContentDto> findAllByIdIn(List<UUID> contentIds);

  void delete(UUID contentId);
}
