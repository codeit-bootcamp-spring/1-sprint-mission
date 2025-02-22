package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.repository.jcf.addOn.UserStatusRepository;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final JCFUserRepository userRepository;

    // DTO로 파라미터 그룹화
    public UserStatus create(UUID userId) {
        if (userStatusRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER_STATUS);
        }
        if (userRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.NO_SUCH_USER);
        }

        return userStatusRepository.save(new UserStatus(userId));

    }

    public Optional<UserStatus> findById(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId);
    }

    public List<UserStatus> findAll(){
        return userStatusRepository.findAll();
    }
//
//    public Map<User, UserStatus> findStatusMapByUserList(List<User> userList){
//        return userStatusRepository.findByUserId(userList);
//    }

    // 이건 DTO가 필요없는거 같은데
    //[ ] userId 로 특정 User의 객체를 업데이트합니다.
    // ??? 오타인걸로 생각 userstatus 업데이트
    public void updateByUserId(UUID userId) {
        UserStatus updatingUserStatus = userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_STATUS_MATCHING_USER));
        updatingUserStatus.update();
        userStatusRepository.save(updatingUserStatus);
    }

    public void delete(UUID statusId) {
        if (userStatusRepository.existsById(statusId)) throw new CustomException(ErrorCode.NO_SUCH_USER_STATUS);
        else userStatusRepository.deleteById(statusId);
    }

    public void deleteByUserId(UUID userId) {
        userStatusRepository.deleteByUserId(userId);
    }
};

