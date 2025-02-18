package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateDTO userStatusCreateDTO) {
        //해당 User 존재 검증
        if(!userRepository.existByUserId(userStatusCreateDTO.userId())) {
            throw new NoSuchElementException("해당 사용자가 존재하지 않습니다. ");
        }

        //해당 User에 대한 UserStatus 객체 존재 검증
        if(userStatusRepository.existsByUserId(userStatusCreateDTO.userId())) {
            throw new NoSuchElementException("해당 사용자에 대한 UserStatus가 이미 존재합니다.");
        }

        UserStatus userStatus = new UserStatus(userStatusCreateDTO);
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus find(UUID uuid) {
        UserStatus userStatus = userStatusRepository.findById(uuid);
        return userStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        List<UserStatus> userStatusList = new ArrayList<>(userStatusRepository.load().values());
        return userStatusList;
    }

    private UserStatus findByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findById(userId);
        return userStatus;
    }


    @Override
    public UserStatus update(UserStatusUpdateDTO userStatusUpdateDTO) {
        UserStatus userStatus = find(userStatusUpdateDTO.id());
        userStatus.update(userStatusUpdateDTO);
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus updateByUserId(UserStatusUpdateDTO userStatusUpdateDTO) {
        UserStatus userStatus = userStatusRepository.findByUserId(userStatusUpdateDTO.userId());
        userStatus.update(userStatusUpdateDTO);
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public void delete(UUID uuid) {
        userStatusRepository.delete(uuid);
    }
}
