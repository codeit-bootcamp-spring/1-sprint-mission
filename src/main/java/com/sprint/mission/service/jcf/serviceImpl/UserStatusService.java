package com.sprint.mission.service.jcf.serviceImpl;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final ExecutorService ves;

    public UserStatus create(User user) {
        if (userStatusRepository.existsByUser(user))
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER_STATUS);
        return userStatusRepository.save(new UserStatus(user));
    }


    @Transactional(readOnly = true)
    public UserStatus findByUserId(UUID userId) {
        return userStatusRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER_STATUS));
    }


    @Transactional(readOnly = true)
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }


    //[ ] userId 로 특정 User의 객체를 업데이트합니다.
    // ??? 오타인걸로 생각 userstatus 업데이트
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

