package com.sprint.mission.discodeit.repository.domain;

import com.sprint.mission.discodeit.domain.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

//유저의 상태
@Repository
public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);
    UserStatus findById(UUID id); //유저 상태
    List<UserStatus> findAll(); //모든 유저 상태
    void delete(UUID id);
    void update(); //유저 상태 업데이트
}
