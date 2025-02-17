package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.HashMap;
import java.util.UUID;

public class JCFUserStatusRepository implements UserStatusRepository {
    HashMap<UUID, UserStatus> userStatusMap = new HashMap<UUID, UserStatus>();

    // 모든 유저스테이터스 객체가 담기는 해쉬맵 반환.
    @Override
    public HashMap<UUID, UserStatus> getUserStatusMap(){
        return userStatusMap;
    }

    //유저 id로 해당 유저의 유저스테이터스 객체 반환
    @Override
    public UserStatus getUserStatusById(UUID userId) {
        return userStatusMap.get(userId);
    }

    //유저 스테이터스 객체 유저스테이터스맵에 추가.
    @Override
    public boolean addUserStatus(UserStatus userStatus) {
        if (userStatus == null) {System.out.println("파라미터에 전달된 userStatus 객체가 null인 상태입니다. "); return false;}
        userStatusMap.put(userStatus.getUserId(), userStatus);
        return true;
    }

    //유저스테이터스맵의 객체 삭제.
    @Override
    public boolean deleteUserStatusById(UUID userId) {
        if (userStatusMap.remove(userId) == null) {System.out.println("해당 userId의 userStatus가 Map에 존재하지 않습니다. "); return false;}
        return true;
    }

    //유저스테이터스맵의 해당 객체 존재 여부 반환.
    @Override
    public boolean isUserStatus(UUID UserId) {
        return userStatusMap.containsKey(UserId);
    }

    //해당 유저스테이터스의 UpdatedAt와 UserStatusType(온라인/오프라인)을 업데이트한다.
    @Override
    public boolean updateUserStatus(UUID userId) {
        UserStatus userStatus = userStatusMap.get(userId);
        if (userStatus == null) {
            System.out.println("해당 userId의 userStatus가 Map에 존재하지 않습니다. "); return false; }
        userStatus.updateStatus();
        return true;
    }
}
