package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService{
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    //유저스테이터스 생성
    @Override
    public UserStatus createUserStatus(UUID userId){
        if (userStatusRepository.isUserStatus(userId)==true) {System.out.println("해당 유저의 UserStatusService는 이미 존재합니다.");}
        UserStatus newUserStatus = new UserStatus(userId);
        return (userStatusRepository.addUserStatus(newUserStatus)) ? newUserStatus : null;
    }

    //유저스테이터스 반환
    @Override
    public UserStatusDto findUserStatusByUserId(UUID userId) {
        if (userId == null) {System.out.println("UserStatus 반환 실패. userId가 null인 상태입니다. 입력값을 확인해주세요."); return null;}
        UserStatus userStatus = userStatusRepository.getUserStatusById(userId);
        return userStatus!=null ? UserStatusDto.from(userStatus) : null;
    }

    //모든 유저스테이터스를 담은 해시맵 반환
    @Override
    public HashMap<UUID, UserStatusDto> findAllUserStatusByUserId() {
        HashMap<UUID, UserStatus> userStatusMap = userStatusRepository.getUserStatusMap();
        if (userStatusMap == null || userStatusMap.isEmpty()) {System.out.println("UserStatusMap 반환 실패. 존재하는 userStatus가 없습니다. "); return null; }
        HashMap<UUID, UserStatusDto> userStatusDtoMap = new HashMap<UUID, UserStatusDto>();
        userStatusMap.keySet().forEach(key -> {
            userStatusDtoMap.put(key, UserStatusDto.from(userStatusMap.get(key)));
        });
        return userStatusDtoMap;
    }

    //해당유저 접속여부 관련 정보를 담은 유저스테이터스 반환
    @Override
    public boolean updateUserStatus(UUID userId) {
        if (userRepository.isUserExistByUUID(userId)==false) {
            System.out.println("UserStatus 업데이트 실패. userId가 null인 상태입니다. 입력값을 확인해주세요. ");
            return false;
        }
        return userStatusRepository.updateUserStatus(userId);
    }

    //유저스테이터스 삭제
    @Override
    public boolean deleteUserStatus(UUID userId) {
        if (userRepository.isUserExistByUUID(userId)==false) {
            System.out.println("UserStatus 삭제 실패. userId가 null인 상태입니다. 입력값을 확인해주세요. ");
            return false;
        }
        return userStatusRepository.deleteUserStatusById(userId);
    }
}
