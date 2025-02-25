package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.status.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.status.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    public UserStatus createUserStatus(CreateUserStatusRequest request){
        if(!userRepository.existsById(request.userId())){
            throw new NoSuchElementException("User not found");
        }
        if (userStatusRepository.findByUserId(request.userId()).isPresent()){
            throw new NoSuchElementException("Userstatus already exists");
        }
        return new UserStatus(request.userId(), request.lastActiveTime());
    }

    public UserStatus getUserStatus(UUID userId){
        return userStatusRepository.findByUserId(userId).orElseThrow(
                () -> new NoSuchElementException("UserStatus not found")
        );
    }

    public UserStatus update(UUID userStatusId, UpdateUserStatusRequest request){
        UserStatus userStatus = userStatusRepository.findById(userStatusId).orElseThrow(
                () -> new NoSuchElementException("UserStatus not found")
        );
        userStatus.updateLastActiveAt(request.lastActiveAt());
        userStatus.updateStatus();
        return userStatusRepository.save(userStatus);
    }

    public void delete(UUID userStatusId){
        UserStatus userStatus = userStatusRepository.findById(userStatusId).orElseThrow(
                () -> new NoSuchElementException("UserStatus not found")
        );
        userStatusRepository.deleteById(userStatusId);
    }
}
