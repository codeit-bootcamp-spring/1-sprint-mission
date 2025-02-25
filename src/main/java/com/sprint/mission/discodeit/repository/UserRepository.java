package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UsersDTO;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    void deleteById(String id);
    Optional<User> findById(String id);
    List<UsersDTO> findAll();

}