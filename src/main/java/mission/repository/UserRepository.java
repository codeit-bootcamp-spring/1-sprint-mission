package mission.repository;

import mission.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User saveUser(User user);
    User findById(UUID id);

//    User updateNamePW(UUID id, String newName, String password);

    List<User> findAll();

    //User updateUserNamePW(User user);

    void validateDuplicateUserName(String userName);

    User findByNamePW(String name, String password);

    User updateUserNamePW(User user);

    void delete(User deletingUser);
}
