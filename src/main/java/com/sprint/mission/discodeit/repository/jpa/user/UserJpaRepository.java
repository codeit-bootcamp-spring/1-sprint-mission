package com.sprint.mission.discodeit.repository.jpa.user;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserJpaRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserByName(String name);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByNameAndPassword(String name, String password);

    @Query("select user from User user")
    List<User> findAll();

    boolean existsByName(String name);
}
