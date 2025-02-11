package com.sprint.mission.discodeit.repository.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.FileStorage;
import com.sprint.mission.discodeit.service.basic.SerializableFileStorage;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {

	private static final Path ROOT_DIR = Paths.get(System.getProperty("user.dir"), "ser");
	private static final String USERSTATUS_FILE = "userstatus.ser";
	private final FileStorage<UserStatus> fileStorage;

	public FileUserStatusRepository() {
		this.fileStorage = new SerializableFileStorage<>(UserStatus.class);
		fileStorage.init(ROOT_DIR);
	}

	/**
	 * 내부적으로 파일에서 전체 UserStatus 목록을 가져옴.
	 */
	private List<UserStatus> findAll() {
		return fileStorage.load(ROOT_DIR.resolve(USERSTATUS_FILE));
	}

	/**
	 * 저장할 때 기존에 동일 id의 상태가 있으면 제거한 후, 새 상태를 추가합니다.
	 */
	@Override
	public UserStatus save(UserStatus userStatus) {
		List<UserStatus> statuses = findAll();
		statuses.removeIf(status -> status.getId().equals(userStatus.getId()));
		statuses.add(userStatus);
		fileStorage.save(ROOT_DIR.resolve(USERSTATUS_FILE), statuses);
		return userStatus;
	}

	@Override
	public Optional<UserStatus> findById(UUID id) {
		List<UserStatus> statuses = findAll();
		for (UserStatus s : statuses) {
			if (s.getId().equals(id)) {
				return Optional.of(s);
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<UserStatus> findByUserId(UUID userId) {
		List<UserStatus> statuses = findAll();
		for (UserStatus s : statuses) {
			if (s.getUserId().equals(userId)) {
				return Optional.of(s);
			}
		}
		return Optional.empty();
	}

	/**
	 * UserStatus의 isOnline() 메서드를 활용하여 현재 온라인 상태인 사용자만 필터링합니다.
	 */
	@Override
	public List<UserStatus> findAllOnlineUsers() {
		List<UserStatus> statuses = findAll();
		List<UserStatus> onlineStatuses = new ArrayList<>();
		for (UserStatus s : statuses) {
			if (s.isOnline()) {
				onlineStatuses.add(s);
			}
		}
		return onlineStatuses;
	}

	@Override
	public void deleteByUserId(UUID userId) {
		List<UserStatus> statuses = findAll();
		statuses.removeIf(status -> status.getUserId().equals(userId));
		fileStorage.save(ROOT_DIR.resolve(USERSTATUS_FILE), statuses);
	}
}
