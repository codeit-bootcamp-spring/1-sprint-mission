package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {

  BinaryContent createBinaryContent(MultipartFile multipartFile);

  BinaryContent createBinaryContent(BinaryContent binaryContent);

  BinaryContentDto findBinaryContentById(UUID binaryContentId);

  List<BinaryContentDto> findAllBinaryContentsById(List<UUID> binaryContentIdList);

  void deleteBinaryContent(UUID binaryContentId);
}
