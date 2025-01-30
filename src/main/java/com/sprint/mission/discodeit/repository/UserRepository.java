package com.sprint.mission.discodeit.repository;

import com.sprint.mission.entity.User;
import java.util.List;

public interface UserRepository {

    User saveUser(User user); // 유저 저장

    void deleteUser(User user); // 유저 삭제

    User findById(String email); // 이메일로 유저 검색

    List<User> printAllUser(); // 모든 유저 조회
}