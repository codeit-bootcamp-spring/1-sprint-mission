package discodeit.service;

import discodeit.entity.User;

import java.util.List;

public interface UserService {
    User create(User newUser);
    User readById(String userId);
    List<User> readAll();
    User update(String userId, User updateUser);
    void delete(String userId);
}
