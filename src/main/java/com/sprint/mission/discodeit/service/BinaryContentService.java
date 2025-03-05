package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.ResponseBinaryContentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BinaryContentService {

    ResponseBinaryContentDto create(MultipartFile multipartFile);

    ResponseBinaryContentDto findById(String contentId);

    List<ResponseBinaryContentDto> findAllByIdIn(List<String> contentIds);

    boolean deleteById(String contentId);

}
