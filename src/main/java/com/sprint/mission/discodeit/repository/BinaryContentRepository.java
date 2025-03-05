package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;

public interface BinaryContentRepository {

    BinaryContent save(BinaryContent binaryContent);

    BinaryContent findById(String id);

    List<BinaryContent> findAll();

    boolean delete(String id);

}
