package com.sprint.mission.service.jcf.serviceImpl;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.repository.UserStatusRepository;
import com.sprint.mission.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserStatusServiceImpl implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public UserStatus create(User user) {
        if (userStatusRepository.existsByUser(user))
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER_STATUS);
        return userStatusRepository.save(new UserStatus(user));
    }

    @Transactional(readOnly = true)
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    public UserStatus updateByUserId(UUID userId) {
        User loginUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));
        UserStatus updatingUserStatus = userStatusRepository.findByUser(loginUser)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_STATUS_MATCHING_USER));
        updatingUserStatus.update();
        return updatingUserStatus;
    }

    public void delete(UUID statusId) {
        if (userStatusRepository.existsById(statusId)) {
            throw new CustomException(ErrorCode.NO_SUCH_USER_STATUS);
        } else {
            userStatusRepository.deleteById(statusId);
        }
    }
};

