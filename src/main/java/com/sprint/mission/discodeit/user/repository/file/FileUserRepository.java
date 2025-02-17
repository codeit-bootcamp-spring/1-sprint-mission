package com.sprint.mission.discodeit.user.repository.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.global.util.FileStorage;
import com.sprint.mission.discodeit.global.util.SerializableFileStorage;
import com.sprint.mission.discodeit.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUserRepository implements UserRepository {
	private final Path rootDir;
	private static final String USER_FILE = "user.ser";
	private final FileStorage<User> fileStorage;

	public FileUserRepository(String fileDirectory) {
		this.rootDir = Paths.get(System.getProperty("user.dir"), fileDirectory);
		this.fileStorage = new SerializableFileStorage<>(User.class);
		fileStorage.init(rootDir);
	}

	@Override
	public User save(User user) {
		List<User> users = findAll();
		users.add(user);
		fileStorage.save(rootDir.resolve(USER_FILE), users);
		// 강제로 다시 로드해보기
		List<User> reloadedUsers = fileStorage.load(rootDir.resolve(USER_FILE));
		log.info("Loaded {} users", reloadedUsers);
		return user;
	}

	@Override
	public Optional<User> findById(UUID id) {
		List<User> users = findAll();
		for (User u : users) {
			if (u.getId().equals(id)) {
				return Optional.of(u);
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<User> findByUserid(String userid) {
		List<User> users = findAll();
		for (User u : users) {
			if (u.getUserid().equals(userid)) {
				return Optional.of(u);
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<User> findByEmail(String email) {
		List<User> users = findAll();
		for (User u : users) {
			if (u.getEmail().equals(email)) {
				return Optional.of(u);
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<User> findByUsername(String username) {
		List<User> users = findAll();
		for (User u : users) {
			if (u.getUserid().equals(username)) {
				return Optional.of(u);
			}
		}
		return Optional.empty();
	}

	@Override
	public List<User> findAll() {
		List<User> users = fileStorage.load(rootDir.resolve(USER_FILE));
		log.info("Loaded users: " + users);
		return users;
	}

	@Override
	public void delete(UUID id) {
		List<User> users = findAll();
		users.removeIf(u -> u.getId().equals(id));
		fileStorage.save(rootDir.resolve(USER_FILE), users);
	}
}