package com.sprint.mission.discodeit.repository.user.interfaces;

import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.Username;

public interface UserRepository {

    User save(User user);

    boolean isExistByEmail(Email email);

    boolean isExistByUsername(Username username);
}
