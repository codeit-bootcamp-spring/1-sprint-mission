package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  BinaryContentResponse create(MultipartFile file);

  BinaryContentResponse findById(UUID id);

  List<BinaryContentResponse> findAllByIdIn(List<UUID> ids);

  void deleteById(UUID id);
}
