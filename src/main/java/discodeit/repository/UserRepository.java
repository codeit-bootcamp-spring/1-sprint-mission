package discodeit.repository;

import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User save(String name, String email, String phoneNumber, String password);
    User find(UUID userId);
    List<User> findAll();
    void update(User user, String name, String email, String phoneNumber);
    void updatePassword(User user, String originalPassword, String newPassword);
    void delete(User user);
}
