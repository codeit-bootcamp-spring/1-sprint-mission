package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(String name, String email, String phoneNumber, String password);
    User find(UUID userId);
    List<User> findAll();
    String getInfo(UUID userId);
    void update(UUID userId, String name, String email, String phoneNumber);
    void updatePassword(UUID userId, String originalPassword, String newPassword);
    void delete(UUID userId);
    void isDuplicateEmail(String email);
    void isDuplicatePhoneNumber(String phoneNumber);
}
