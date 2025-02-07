package com.sprint.mission.discodeit.repository.file;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
	@Override
	public ReadStatus save(ReadStatus readStatus) {
		return null;
	}

	@Override
	public ReadStatus findById(Long id) {
		return null;
	}

	@Override
	public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
		return Optional.empty();
	}

	@Override
	public List<ReadStatus> findAllByUserId(UUID userId) {
		return List.of();
	}

	@Override
	public List<ReadStatus> findAllByChannelId(UUID channelId) {
		return List.of();
	}

	@Override
	public void deleteByUserIdAndChannelId(UUID userId, UUID channelId) {

	}
}
