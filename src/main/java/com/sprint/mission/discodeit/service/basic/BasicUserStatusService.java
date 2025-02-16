package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService{
    UserStatusRepository userStatusRepository;
    UserRepository userRepository;

    @Override
    public UserStatus createUserStatus(UUID userId){
        if (userRepository.isUserExistByUUID(userId)) {
            throw new NoSuchElementException("해당 유저가 유저레포지토리에 존재하지 않습니다.");
        }
        if (userStatusRepository.isUserStatus(userId)==true) {
            throw new RuntimeException("해당 유저의 UserStatusService는 이미 존재합니다.");
        }
        UserStatus newUserStatus = new UserStatus(userId);
        userStatusRepository.addUserStatus(newUserStatus);
        return newUserStatus;
    }

    @Override
    public UserStatusDto findUserStatusByUserId(UUID userId) {
        if (userId == null) {
            System.err.println("UserStatus 반환 실패. userId가 null인 상태입니다. 입력값을 확인해주세요.");
            return null;
        }
        UserStatus userStatus = userStatusRepository.getUserStatusById(userId);
        return userStatus!=null ? UserStatusDto.from(userStatus) : null;
    }

    @Override
    public HashMap<UUID, UserStatusDto> findAllUserStatusByUserId() {
        HashMap<UUID, UserStatus> userStatusMap = userStatusRepository.getUserStatusMap();
        if (userStatusMap == null||userStatusMap.isEmpty()) {
            System.err.println("UserStatusMap 반환 실패. 존재하는 userStatus가 없습니다. ");
            return null;
        }
        HashMap<UUID, UserStatusDto> userStatusDtoMap = new HashMap<UUID, UserStatusDto>();
        userStatusMap.keySet().forEach(key -> {
            userStatusDtoMap.put(key, UserStatusDto.from(userStatusMap.get(key)));
        });
        return userStatusDtoMap;
    }

    @Override
    public boolean updateUserStatus(UUID userId) {
        if (userRepository.isUserExistByUUID(userId)==false) {
            System.err.println("UserStatus 업데이트 실패. userId가 null인 상태입니다. 입력값을 확인해주세요. ");
            return false;
        }
        return userStatusRepository.updateUserStatus(userId);
    }

    @Override
    public boolean deleteUserStatus(UUID userId) {
        if (userRepository.isUserExistByUUID(userId)==false) {
            System.err.println("UserStatus 삭제 실패. userId가 null인 상태입니다. 입력값을 확인해주세요. ");
            return false;
        }
        return userStatusRepository.deleteUserStatusById(userId);
    }
}
