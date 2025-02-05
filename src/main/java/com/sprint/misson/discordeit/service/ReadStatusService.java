package com.sprint.misson.discordeit.service;

import com.sprint.misson.discordeit.entity.status.ReadStatus;

import java.util.List;

public interface ReadStatusService {

    ReadStatus findById(String userStatusId);

    List<ReadStatus> findAll();

    boolean update(ReadStatus userStatus);

    boolean delete(String userStatusId);
}
