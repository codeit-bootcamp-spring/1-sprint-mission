package discodeit.service;

import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void create(User user);
    User read(UUID id);
    List<User> readAll();
    void update(UUID id, String username);
    void delete(UUID id);
}
