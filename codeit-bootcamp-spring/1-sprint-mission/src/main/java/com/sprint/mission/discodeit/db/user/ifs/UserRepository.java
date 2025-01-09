package com.sprint.mission.discodeit.db.user.ifs;

import com.sprint.mission.discodeit.entity.user.User;
import java.util.Optional;

public interface UserRepository {

    // 이름으로 찾기
    Optional<User> findByUsername(String username);
}
