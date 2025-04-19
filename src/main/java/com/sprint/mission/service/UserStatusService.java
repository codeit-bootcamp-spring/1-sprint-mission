package com.sprint.mission.service;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(User user);
    List<UserStatus> findAll() ;
    UserStatus updateByUserId(UUID userId);
    void delete(UUID statusId);
}
