package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void create(User user); // 사용자 생성
    Optional<User> read(UUID id); // 사용자 조회
    List<User> readAll(); // 모든 사용자 조회
    void update(UUID id, User user); // 사용자 정보 수정
    void delete(UUID id); // 사용자 삭제
}
