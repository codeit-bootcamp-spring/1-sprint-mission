package com.sprint.mission.discodeit.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.user.entity.User;

public interface UserRepository {
	User save(User user);

	Optional<User> findById(UUID id);

	Optional<User> findByUserid(String userid);

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);

	List<User> findAll();

	void delete(UUID id);
}
