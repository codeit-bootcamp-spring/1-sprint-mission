package discodeit.service;

import discodeit.entity.User;

import java.util.UUID;

public interface UserService {
    User create();
    User read(UUID key);
    User update(UUID key, User userToUpdate);
    User delete(UUID key);
}
