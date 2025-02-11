package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

//유저의 상태
@Repository
public interface UserStatusRepository {
    UserStatus save(UserStatusDto userStatusDto);
    UserStatus findById(UUID id); // 고유 UserState의 아디디 값으로 찾기
    UserStatus findByUserId(UUID userId); // 유저 아이디로 찾기
    List<UserStatus> findAll(); //모든 유저 상태
    void delete(UUID id);
    void update(UserStatusDto before, UserStatusDto after); //유저 상태 업데이트


}
