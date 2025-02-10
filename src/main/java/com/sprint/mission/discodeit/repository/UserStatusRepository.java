package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusRepository {
    //이건 모든객체해쉬맵으로 모아서 직렬화하는것보다 객체 자체를 직렬화하는게 나을듯
    public UserStatus getUserStatus(UUID UserStatusId);
    public boolean addUserStatus(UserStatus UserStatus);
    public boolean deleteUserStatus(UUID UserStatusId);
    public boolean isUserStatus(UUID UserStatusId);
    //업데이트 메소드의 필요성을 느끼지 못하여 우선은 C, R, D만 구현
}
