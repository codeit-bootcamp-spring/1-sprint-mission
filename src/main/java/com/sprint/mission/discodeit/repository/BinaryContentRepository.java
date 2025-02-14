package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;

public interface BinaryContentRepository {

    BinaryContent save(CreateBinaryContentDto createBinaryContentDto);

    BinaryContent findById(String id);

    List<BinaryContent> findAll();

    boolean delete(String id);

}
