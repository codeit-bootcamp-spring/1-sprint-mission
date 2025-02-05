package com.sprint.misson.discordeit.service.basic;

import com.sprint.misson.discordeit.entity.status.UserStatus;
import com.sprint.misson.discordeit.repository.UserStatusRepository;
import com.sprint.misson.discordeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatus findById(String userStatusId) {
        return userStatusRepository.findById(userStatusId);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus create(UserStatus userStatus) {
        return userStatusRepository.save(userStatus);
    }

    @Override
    public boolean updateByUserId(String userStatusId, UserStatus userStatus) {
        return false;
    }

    @Override
    public boolean delete(String userStatusId) {
        return false;
    }
}
