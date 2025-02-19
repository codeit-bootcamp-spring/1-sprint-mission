package com.sprint.mission.discodeit.repository.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.FileStorage;
import com.sprint.mission.discodeit.service.basic.SerializableFileStorage;

public class FileReadStatusRepository implements ReadStatusRepository {
	// 루트 디렉터리 및 파일 경로 정의
	private static final String READSTATUS_FILE = "readstatus.ser";
	private final Path rootDir;
	private final FileStorage<ReadStatus> fileStorage;

	public FileReadStatusRepository(String fileDirectory) {
		this.rootDir = Paths.get(System.getProperty("user.dir"), fileDirectory);
		this.fileStorage = new SerializableFileStorage<>(ReadStatus.class);
		fileStorage.init(rootDir);
	}

	private List<ReadStatus> findAll() {
		return fileStorage.load(rootDir.resolve(READSTATUS_FILE));
	}

	/**
	 * 새 ReadStatus를 저장.
	 * 동일 ID가 존재하면 업데이트하고, 없으면 새 항목을 추가.
	 */
	@Override
	public ReadStatus save(ReadStatus readStatus) {
		List<ReadStatus> statuses = findAll();
		boolean updated = false;
		for (int i = 0; i < statuses.size(); i++) {
			if (statuses.get(i).getId().equals(readStatus.getId())) {
				statuses.set(i, readStatus);
				updated = true;
				break;
			}
		}
		if (!updated) {
			statuses.add(readStatus);
		}
		fileStorage.save(rootDir.resolve(READSTATUS_FILE), statuses);
		return readStatus;
	}

	@Override
	public Optional<ReadStatus> findById(UUID id) {
		List<ReadStatus> statuses = findAll();
		for (ReadStatus status : statuses) {
			if (status.getId().equals(id)) {
				return Optional.of(status);
			}
		}
		return Optional.empty();
	}

	/**
	 * 특정 사용자(userId)와 채널(channelId)에 해당하는 ReadStatus를 조회.
	 */
	@Override
	public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
		List<ReadStatus> statuses = findAll();
		for (ReadStatus s : statuses) {
			if (s.getUserId().equals(userId) && s.getChannelId().equals(channelId)) {
				return Optional.of(s);
			}
		}
		return Optional.empty();
	}

	/**
	 * 특정 사용자(userId)에 해당하는 모든 ReadStatus를 조회.
	 */
	@Override
	public List<ReadStatus> findAllByUserId(UUID userId) {
		List<ReadStatus> statuses = findAll();
		List<ReadStatus> result = new ArrayList<>();
		for (ReadStatus s : statuses) {
			if (s.getUserId().equals(userId)) {
				result.add(s);
			}
		}
		return result;
	}

	/**
	 * 특정 채널(channelId)에 해당하는 모든 ReadStatus를 조회합니다.
	 */
	@Override
	public List<ReadStatus> findAllByChannelId(UUID channelId) {
		List<ReadStatus> statuses = findAll();
		List<ReadStatus> result = new ArrayList<>();
		for (ReadStatus s : statuses) {
			if (s.getChannelId().equals(channelId)) {
				result.add(s);
			}
		}
		return result;
	}

	/**
	 * 특정 사용자와 채널 조합에 해당하는 ReadStatus를 삭제합니다.
	 */
	@Override
	public void deleteByUserIdAndChannelId(UUID userId, UUID channelId) {
		List<ReadStatus> statuses = findAll();
		statuses.removeIf(s -> s.getUserId().equals(userId) && s.getChannelId().equals(channelId));
		fileStorage.save(rootDir.resolve(READSTATUS_FILE), statuses);
	}

	/**
	 * 특정 채널(channelId)에 해당하는 모든 ReadStatus를 삭제합니다.
	 */
	@Override
	public void deleteAllByChannelId(UUID channelId) {
		List<ReadStatus> statuses = findAll();
		statuses.removeIf(s -> s.getChannelId().equals(channelId));
		fileStorage.save(rootDir.resolve(READSTATUS_FILE), statuses);
	}
}
