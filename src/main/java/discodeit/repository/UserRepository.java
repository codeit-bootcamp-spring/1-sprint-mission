package discodeit.repository;

import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User save(String name, String email, String phoneNumber, String password);
    User find(UUID userId);
    List<User> findAll();
    void update(UUID userId, String name, String email, String phoneNumber);
    void updatePassword(UUID userId, String originalPassword, String newPassword);
    void delete(UUID userId);
}
