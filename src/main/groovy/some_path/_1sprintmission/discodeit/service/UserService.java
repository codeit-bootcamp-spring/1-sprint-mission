package some_path._1sprintmission.discodeit.service;
import some_path._1sprintmission.discodeit.entiry.User;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
public interface UserService {
    void createUser(User user);
    Optional<User> getUser(UUID id);
    List<User> getUserAll();
    void updateUser(UUID id, User updatedUser);
    void deleteUser(UUID id);


    int makeDiscriminator(); //디스코드 이름#+4자리 숫자, 숫자 생성

}