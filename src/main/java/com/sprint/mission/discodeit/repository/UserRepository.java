package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.Map;
import java.util.UUID;

public interface UserRepository {

    Long save(User user);

    User load(Long id);

    Map.Entry<Long, User> load(UUID uuid);

    Map<Long, User> loadAll();

    void delete(Long id);

    void update(Long id, User user);

    // 검색 조건 달린 거 (index Id 제외)
    Map.Entry<Long, User> findUserByUserName(String userName);

    // 사용자 존재 여부 확인
    boolean isExistByUserName(String userName);

    boolean isExistByEmail(String email);

    boolean confirmLogin(String userName, String password);

}
