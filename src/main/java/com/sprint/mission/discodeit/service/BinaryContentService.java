package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;

public interface BinaryContentService {

    BinaryContent create(CreateBinaryContentDto createBinaryContentDto);

    BinaryContent findById(String contentId);

    List<BinaryContent> findAllByIdIn(List<String> contentIds);

    boolean deleteById(String contentId);

}
