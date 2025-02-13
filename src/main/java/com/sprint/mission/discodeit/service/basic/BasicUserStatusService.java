package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserServiceFindDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.UserStatusType;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;

    @Override
    public UUID create(UserStatusCreateDTO dto) {

        //Duplicate >> 서비스 계층에서 >>레포.findByUserId() 해서 있으면 중복
        checkDuplicateUserStatus(dto.getUserid());

        UserStatus userStatus = new UserStatus(dto.getUserid());
        return userStatusRepository.save(userStatus);
    }


    @Override
    public UserStatus find(UUID id) {
        UserStatus findUserStatus = userStatusRepository.findOne(id);
        Optional.ofNullable(findUserStatus)
                .orElseThrow(() -> new NoSuchElementException("해당 UserStatus 가 없습니다."));
        return findUserStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UserStatusUpdateDTO userStatusUpdateDTO) {
        UserStatus findUserStatus = userStatusRepository.findOne(userStatusUpdateDTO.getId());
        findUserStatus.updateUserStatus(userStatusUpdateDTO.getType());
        userStatusRepository.update(findUserStatus);
        return findUserStatus;
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusType type) {
        UserStatus finduserStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 userid:" + userId + "의 UserStatus 가 없습니다."));

        finduserStatus.updateUserStatus(type);
        userStatusRepository.update(finduserStatus);
        return finduserStatus;
    }

    @Override
    public UUID delete(UUID id) {
        return userStatusRepository.delete(id);
    }

    private void checkDuplicateUserStatus(UUID userId) {

        if (userStatusRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("중복된 UserStatus 가 존재합니다. Userid: " + userId);
        }

    }
}
