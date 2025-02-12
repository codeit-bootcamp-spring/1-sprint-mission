package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

public class JCFUserRepository implements UserRepository {
	private final Map<UUID, User> userData = new HashMap<>();

	@Override
	public User save(User user) {
		userData.put(user.getId(), user);
		return user;
	}

	@Override
	public Optional<User> findById(UUID id) {
		return Optional.ofNullable(userData.get(id));
	}

	@Override
	public Optional<User> findByUserid(String userid) {
		for (User user : userData.values()) {
			if (user.getUserid().equals(userid)) {
				return Optional.of(user);
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<User> findByEmail(String email) {
		for (User user : userData.values()) {
			if (user.getEmail().equals(email)) {
				return Optional.of(user);
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<User> findByUsername(String username) {
		for (User user : userData.values()) {
			if (user.getUsername().equals(username)) {
				return Optional.of(user);
			}
		}
		return Optional.empty();
	}

	@Override
	public List<User> findAll() {
		return new ArrayList<>(userData.values());
	}

	@Override
	public void delete(UUID id) {
		userData.remove(id);
	}
}
