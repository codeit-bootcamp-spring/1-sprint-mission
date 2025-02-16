package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.binaryContent.ResponseBinaryContentDto;

import java.util.List;

public interface BinaryContentService {

    ResponseBinaryContentDto create(CreateBinaryContentDto createBinaryContentDto);

    ResponseBinaryContentDto findById(String contentId);

    List<ResponseBinaryContentDto> findAllByIdIn(List<String> contentIds);

    boolean deleteById(String contentId);

}
