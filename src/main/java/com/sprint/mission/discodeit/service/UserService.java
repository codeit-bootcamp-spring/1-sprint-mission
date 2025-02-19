package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateDTO;
import com.sprint.mission.discodeit.dto.UserReadDTO;
import com.sprint.mission.discodeit.dto.UserUpdateDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void create(UserCreateDTO userDTO);
    Optional<UserReadDTO> read(UUID id);
    List<UserReadDTO> readAll();
    void update(UUID id, UserUpdateDTO userDTO);
    void delete(UUID id);

    // ✅ 사용자의 마지막 활동 시간 업데이트
    boolean updateLastSeen(UUID userId);

    // ✅ 프로필 이미지 업데이트 (새로운 기능 추가)
    void updateProfileImage(UUID userId, UUID imageId);
}
