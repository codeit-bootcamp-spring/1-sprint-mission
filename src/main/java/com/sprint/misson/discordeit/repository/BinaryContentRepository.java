package com.sprint.misson.discordeit.repository;

import com.sprint.misson.discordeit.entity.BinaryContent;

import java.util.List;

public interface BinaryContentRepository {

    BinaryContent save(BinaryContent binaryContent);

    BinaryContent findById(String id);

    List<BinaryContent> findAll();

    boolean delete(String id);

}
