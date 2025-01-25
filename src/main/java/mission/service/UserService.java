package mission.service;


import mission.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {
    void createOrUpdate(User user) throws IOException;
    void update(User user);
    User findById(UUID id);
    Set<User> findAll();
    void delete(User user);
    //void validateDuplicateName(String name);
}
