package mission.repository;

import mission.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserRepository {
    User save(User user) throws IOException;
    User findById(UUID id) throws IOException, ClassNotFoundException;
    Set<User> findAll() throws IOException;
    void delete(User deletingUser) throws IOException;

    //User updateUserNamePW(User user) throws IOException;
}
