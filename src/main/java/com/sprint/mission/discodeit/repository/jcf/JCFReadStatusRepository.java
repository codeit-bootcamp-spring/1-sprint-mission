package com.sprint.mission.discodeit.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

public class JCFReadStatusRepository implements ReadStatusRepository {
	private final Map<UUID, ReadStatus> readStatusData = new HashMap<>();

	@Override
	public ReadStatus save(ReadStatus readStatus) {
		// 동일한 ID가 있으면 값을 업데이트하고, 없으면 새 항목 추가
		readStatusData.put(readStatus.getId(), readStatus);
		return readStatus;
	}

	@Override
	public Optional<ReadStatus> findById(UUID id) {
		return Optional.ofNullable(readStatusData.get(id));
	}

	@Override
	public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
		for (ReadStatus status : readStatusData.values()) {
			if (status.getUserId().equals(userId) && status.getChannelId().equals(channelId)) {
				return Optional.of(status);
			}
		}
		return Optional.empty();
	}

	@Override
	public List<ReadStatus> findAllByUserId(UUID userId) {
		List<ReadStatus> result = new ArrayList<>();
		for (ReadStatus status : readStatusData.values()) {
			if (status.getUserId().equals(userId)) {
				result.add(status);
			}
		}
		return result;
	}

	@Override
	public List<ReadStatus> findAllByChannelId(UUID channelId) {
		List<ReadStatus> result = new ArrayList<>();
		for (ReadStatus status : readStatusData.values()) {
			if (status.getChannelId().equals(channelId)) {
				result.add(status);
			}
		}
		return result;
	}

	@Override
	public void deleteByUserIdAndChannelId(UUID userId, UUID channelId) {
		List<UUID> idsToRemove = new ArrayList<>();
		for (ReadStatus status : readStatusData.values()) {
			if (status.getUserId().equals(userId) && status.getChannelId().equals(channelId)) {
				idsToRemove.add(status.getId());
			}
		}
		for (UUID id : idsToRemove) {
			readStatusData.remove(id);
		}
	}

	@Override
	public void deleteAllByChannelId(UUID channelId) {
		List<UUID> idsToRemove = new ArrayList<>();
		for (ReadStatus status : readStatusData.values()) {
			if (status.getChannelId().equals(channelId)) {
				idsToRemove.add(status.getId());
			}
		}
		for (UUID id : idsToRemove) {
			readStatusData.remove(id);
		}
	}
}
