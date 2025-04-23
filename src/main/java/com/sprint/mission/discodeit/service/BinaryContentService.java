package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

  // 프로필 사진 생성
  BinaryContentDto create(CreateBinaryContentRequest request);

  // 프로필 사진 단건 검색
  BinaryContentDto find(UUID binaryContentId);

  // 프로필 사진 다건 검색
  List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds);

  // 프로필 사진 삭제
  void delete(UUID binaryContentId);
}
