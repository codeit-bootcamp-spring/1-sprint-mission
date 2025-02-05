package com.sprint.misson.discordeit.service.basic;

import com.sprint.misson.discordeit.entity.status.ReadStatus;
import com.sprint.misson.discordeit.repository.ReadStatusRepository;
import com.sprint.misson.discordeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatus findById(String userStatusId) {
        return null;
    }

    @Override
    public List<ReadStatus> findAll() {
        return List.of();
    }

    @Override
    public boolean update(ReadStatus userStatus) {
        return false;
    }

    @Override
    public boolean delete(String userStatusId) {
        return false;
    }
}
