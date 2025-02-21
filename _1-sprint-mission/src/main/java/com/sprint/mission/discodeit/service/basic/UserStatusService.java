package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public UserStatus create(UserStatusDto dto) {
        //User 존재하지 않으면 예외 발생
        if(!userRepository.existsById(dto.user().getId())){
            throw new IllegalArgumentException("user not found");
        }
        //User와 관련된 객체가 이미 존재하면 예외 발생
        Optional<UserStatus> exist = userStatusRepository.findById(dto.user().getId());
        if(exist.isPresent()){
            throw new IllegalArgumentException("user already exists");
        }
        UserStatus userStatus = new UserStatus(dto.user(),dto.lastConnetTime(),dto.onelineStatus());
        return userStatusRepository.save(userStatus);
    }

    public UserStatus findById(UUID userStatusid) {
        return userStatusRepository.findById(userStatusid).orElseThrow(()->new NoSuchElementException("userStatus not found"));
    }

    public List<UserStatus> findAll(){
        return userStatusRepository.findAll();
    }

    public UserStatus update(UpdateUserStatusDto dto){
        UserStatus userStatus = userStatusRepository.findById(dto.user().getId()).orElseThrow(()->new NoSuchElementException("user not found"));

        userStatus.updateLastConnectTime();
        userStatus.loginUserCheck();

        return userStatusRepository.save(userStatus);

    }

    public UserStatus updateByUserId(UUID userId){
        UserStatus userStatus = userStatusRepository.findById(userId).orElseThrow(()->new NoSuchElementException("user not found"));
        userStatus.updateLastConnectTime();
        userStatus.loginUserCheck();

        User user = userStatus.getUser();
        userRepository.save(user);

        return userStatusRepository.save(userStatus);
    }

    public void delete(UUID userId){
        if(!userStatusRepository.existsById(userId)){
            throw new NoSuchElementException("user not found");
        }
        userStatusRepository.deleteById(userId);
    }
}
