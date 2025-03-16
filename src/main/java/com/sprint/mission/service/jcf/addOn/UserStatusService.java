package com.sprint.mission.service.jcf.addOn;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final ExecutorService ves;

    // DTO로 파라미터 그룹화
    public UserStatus create(User user) {
        if (userStatusRepository.existsByUser(user))
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER_STATUS);
        return userStatusRepository.save(new UserStatus(user));
    }

    // 나중에 바꾸기
    public UserStatus findByUserId(UUID userId) {
        return userStatusRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER_STATUS));
    }

    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    // 이건 DTO가 필요없는거 같은데
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
//    public void deleteByUserId(UUID userId) {
//        userStatusRepository.delete(userId);
//    }

//    public List<User> findStatusMapByUserList() {
//         return userRepository.findAllWithRelations();
//    }


//    public Map<User, Boolean> findStatusMapByUserList() {
//        Map<User, Boolean> userStatusMap = new HashMap<>();
//        userRepository.findAllFetch().forEach(user -> {
//            userStatusMap.put(user, user.getStatus().isOnline());
//        });
//        userRepository.findAll().forEach(user -> {
//            Boolean isOnline = userStatusRepository.findByUser(user)
//                    .map(UserStatus::isOnline)
//                    .orElse(false);
//            userStatusMap.put(user, isOnline);
//            // Optional 원칙 : isPresent()는 최대한 사용하지 말것 => if-else랑 비슷해서 가독성 떨어지고 Optional의 장점이 사라짐
//        return userStatusMap;
//    }
};

