package some_path._1sprintmission.discodeit.repository;


import some_path._1sprintmission.discodeit.entiry.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    void saveAll(List<User> users);
}