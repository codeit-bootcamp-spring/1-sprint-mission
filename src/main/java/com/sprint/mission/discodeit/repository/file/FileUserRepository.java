package com.sprint.mission.discodeit.repository.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.FileStorage;
import com.sprint.mission.discodeit.service.file.SerializableFileStorage;

@Repository
public class FileUserRepository implements UserRepository {
	private static final Path ROOT_DIR = Paths.get(System.getProperty("user.dir"), "tmp");
	private static final String USER_FILE = "user.ser";
	private final FileStorage<User> fileStorage;

	public FileUserRepository() {
		this.fileStorage = new SerializableFileStorage<>(User.class);
		fileStorage.init(ROOT_DIR);
	}

	@Override
	public User save(User user) {
		List<User> users = findAll();
		users.add(user);
		fileStorage.save(ROOT_DIR.resolve(USER_FILE), users);
		return user;
	}

	@Override
	public Optional<User> findById(UUID id) {
		return findAll().stream()
			.filter(u -> u.getId().equals(id))
			.findFirst();
	}

	@Override
	public Optional<User> findByUserid(String userid) {
		return findAll().stream()
			.filter(u -> u.getUserid().equals(userid))
			.findFirst();
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return findAll().stream()
			.filter(u -> u.getEmail().equals(email))
			.findFirst();
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return findAll().stream()
			.filter(u -> u.getUserid().equals(username))
			.findFirst();
	}

	@Override
	public List<User> findAll() {
		return fileStorage.load(ROOT_DIR);
	}

	@Override
	public void delete(UUID id) {
		List<User> users = findAll();
		users.removeIf(u -> u.getId().equals(id));
		fileStorage.save(ROOT_DIR.resolve(USER_FILE), users);
	}
}
