package com.sprint.mission.discodeit.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.user.entity.User;

public interface UserRepository {

	User save(User user);

	Optional<User> findById(UUID id);

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	List<User> findAll();

	boolean existsById(UUID id);

	void deleteById(UUID id);

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);
}