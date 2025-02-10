package com.sprint.mission.discodeit.service.basic;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
	private final ReadStatusRepository readStatusRepository;
	private final UserRepository userRepository;
	private final ChannelRepository channelRepository;

	@Override
	public ReadStatus create(CreateReadStatusRequest request) {
		// 관련 엔티티 존재 확인
		userRepository.findById(request.userId())
			.orElseThrow(() -> new IllegalArgumentException("User not found"));
		channelRepository.findById(request.channelId())
			.orElseThrow(() -> new IllegalArgumentException("Channel not found"));

		// 중복 체크
		if (readStatusRepository.findByUserIdAndChannelId(
			request.userId(), request.channelId()).isPresent()) {
			throw new IllegalArgumentException("ReadStatus already exists");
		}

		ReadStatus readStatus = new ReadStatus(
			request.userId(),
			request.channelId(),
			request.lastReadAt()
		);
		return readStatusRepository.save(readStatus);
	}

	@Override
	public ReadStatus find(UUID id) {
		return readStatusRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));
	}

	@Override
	public List<ReadStatus> findAllByUserId(UUID userId) {
		return readStatusRepository.findAllByUserId(userId);
	}

	@Override
	public ReadStatus update(UUID id, UpdateReadStatusRequest request) {
		ReadStatus readStatus = readStatusRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));

		readStatus.updateLastReadAt(request.lastReadAt());
		return readStatusRepository.save(readStatus);
	}

	@Override
	public void delete(UUID id) {
		ReadStatus status = readStatusRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));
		readStatusRepository.deleteByUserIdAndChannelId(status.getUserId(), status.getChannelId());
	}
}
