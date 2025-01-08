package sprint.mission.discodeit.service;

import sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserService {
    User create(User userToCreate);
    User read(UUID key);
    User update(UUID key, User userToUpdate);
    User delete(UUID key);
}
