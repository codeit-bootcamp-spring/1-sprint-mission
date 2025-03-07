package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BinaryContentService {

  BinaryContentDto create(MultipartFile multipartFile);

  BinaryContentDto findById(String contentId);

  List<BinaryContentDto> findAllByIdIn(List<String> contentIds);

  boolean deleteById(String contentId);

}
