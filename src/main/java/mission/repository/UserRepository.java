package mission.repository;

import mission.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User saveUser(User user);
    User findById(UUID id);
    List<User> findAll();
}
