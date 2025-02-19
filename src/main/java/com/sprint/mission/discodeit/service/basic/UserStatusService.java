package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DuplicateException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public UserStatus create(UUID userId) {
        userRepository.findById(userId);
        List<UserStatus> userStatuses = userStatusRepository.findAll();

        for (UserStatus userStatus : userStatuses) {
            if (userStatus.getUserId().equals(userId)) {
                throw new DuplicateException("이미 존재하는 user에 대한 userStatus 생성");
            }
        }

        UserStatus userStatus = UserStatus.from(userId);
        return userStatusRepository.save(userStatus);
    }

    public UserStatus findById(UUID userId) {
        return userStatusRepository.findById(userId);
    }

    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    public void delete(UUID id) {
        userStatusRepository.delete(id);
    }
}
