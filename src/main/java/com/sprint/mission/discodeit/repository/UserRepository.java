package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    User findbyId(UUID uuid);

    Boolean existByUserId(UUID userId);
    boolean existsByUsername(String username);

    Map<UUID, User> load();
    void delete(UUID id);

    List<UUID> findAllUserIdByChannelId(UUID uuid); //채널에 참여하는 유저 id 리스트 반환

}
