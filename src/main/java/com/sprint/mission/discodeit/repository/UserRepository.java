package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserRepository {
    // 저장
    boolean saveUser(User user);

    // 조회
    User loadUser(User user);
    List<User> loadAllUser();

    // 삭제
    boolean deleteUser(User user);

}
