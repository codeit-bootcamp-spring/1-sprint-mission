package com.sprint.mission.discodeit.user.repository.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.global.util.FileStorage;
import com.sprint.mission.discodeit.global.util.JsonFileStorage;
import com.sprint.mission.discodeit.user.entity.UserStatus;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUserStatusRepository implements UserStatusRepository {

	private final Path rootDir;
	private static final String USERSTATUS_FILE = "userstatus.json";
	private final FileStorage<UserStatus> fileStorage;

	public FileUserStatusRepository(String fileDirectory) {
		this.rootDir = Paths.get(System.getProperty("user.dir"), fileDirectory);
		this.fileStorage = new JsonFileStorage<>(UserStatus.class);
		fileStorage.init(rootDir);
	}

	/**
	 * 저장할 때 기존에 동일 id의 상태가 있으면 제거한 후, 새 상태를 추가합니다.
	 */
	@Override
	public UserStatus save(UserStatus userStatus) {
		List<UserStatus> statuses = findAll();
		statuses.removeIf(status -> status.getId().equals(userStatus.getId()));
		statuses.add(userStatus);
		fileStorage.save(rootDir.resolve(USERSTATUS_FILE), statuses);
		return userStatus;
	}

	/**
	 * 내부적으로 파일에서 전체 UserStatus 목록을 가져옴.
	 */
	@Override
	public List<UserStatus> findAll() {
		Path filePath = rootDir.resolve(USERSTATUS_FILE);

	/*	// 파일이 디렉토리인지 체크 후 삭제
		if (Files.exists(filePath)) {
			log.error("🚨 오류: userstatus.json이 디렉토리로 생성됨. 삭제 후 재생성합니다.");
			try {
				Files.delete(filePath);
			} catch (IOException e) {
				throw new RuntimeException("디렉토리 삭제 실패: " + filePath, e);
			}
		}
*/
		List<UserStatus> userStatuses = fileStorage.load(filePath);
		return userStatuses;
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

	@Override
	public boolean existsById(UUID id) {
		return findById(id).isPresent();
	}

	@Override
	public void deleteById(UUID id) {
		List<UserStatus> statuses = findAll();
		statuses.removeIf(status -> status.getId().equals(id));
		fileStorage.save(rootDir.resolve(USERSTATUS_FILE), statuses);
	}

	@Override
	public void deleteByUserId(UUID userId) {
		List<UserStatus> statuses = findAll();
		statuses.removeIf(status -> status.getUserId().equals(userId));
		fileStorage.save(rootDir.resolve(USERSTATUS_FILE), statuses);
	}
}
