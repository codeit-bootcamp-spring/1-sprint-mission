package com.sprint.mission.discodeit.repository.user.interfaces;

import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.Username;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findOneById(UUID id);

    Optional<User> findOneByEmail(Email email);

    List<User> findAll();

    boolean isExistByEmail(Email email);

    boolean isExistByUsername(Username username);

    void deleteByUser(User user);
}
