package com.sprint.misson.discordeit.repository;

import com.sprint.misson.discordeit.entity.status.ReadStatus;

import java.util.List;

public interface ReadStatusRepository {

    ReadStatus save(ReadStatus readStatus);

    ReadStatus findById(String id);

    List<ReadStatus> findAll();

    boolean delete(String id);

}
