package com.sprint.mission.discodeit.readStatus.service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.readStatus.entity.ReadStatus;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
	private final ReadStatusRepository readStatusRepository;
	private final UserRepository userRepository;
	private final ChannelRepository channelRepository;

	/**
	 * 새로운 읽음 상태를 생성합니다.
	 * @param request 읽음 상태 생성 요청 정보 (사용자 ID, 채널 ID, 메시지 ID, 마지막 읽은 시각)
	 * @return 생성된 읽음 상태
	 * @throws IllegalArgumentException 사용자나 채널이 존재하지 않거나, 이미 읽음 상태가 존재하는 경우
	 */
	@Override
	public ReadStatus create(ReadStatusCreateRequest request) {
		UUID userId = request.userId();
		UUID channelId = request.channelId();

		if (!userRepository.existsById(userId)) {
			throw new NoSuchElementException("User with id " + userId + " does not exist");
		}
		if (!channelRepository.existsById(channelId)) {
			throw new NoSuchElementException("Channel with id " + channelId + " does not exist");
		}
		if (readStatusRepository.findAllByUserId(userId).stream()
			.anyMatch(readStatus -> readStatus.getChannelId().equals(channelId))) {
			throw new IllegalArgumentException(
				"ReadStatus with userId " + userId + " and channelId " + channelId + " already exists");
		}

		Instant lastReadAt = request.lastReadAt();
		ReadStatus readStatus = new ReadStatus(userId, channelId, lastReadAt);
		return readStatusRepository.save(readStatus);
	}

	/**
	 * ID로 읽음 상태를 조회합니다.
	 * @param id 읽음 상태 ID
	 * @return 읽음 상태
	 * @throws IllegalArgumentException 읽음 상태가 존재하지 않는 경우
	 */
	@Override
	public ReadStatus find(UUID id) {
		return readStatusRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));
	}

	/**
	 * 사용자 ID로 해당 사용자의 모든 읽음 상태를 조회합니다.
	 * @param userId 사용자 ID
	 * @return 읽음 상태 목록
	 */
	@Override
	public List<ReadStatus> findAllByUserId(UUID userId) {
		return readStatusRepository.findAllByUserId(userId);
	}

	/**
	 * 읽음 상태를 업데이트합니다.
	 * @param request 상태 업데이트 요청 정보 (특정 유저, 채널과 마지막 읽은 시각)
	 * @return 업데이트된 읽음 상태
	 * @throws IllegalArgumentException 읽음 상태가 존재하지 않는 경우
	 */
	@Override
	public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
		// 읽음 상태 조회 및 업데이트
		/*ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(request.userId(), request.channelId())
			.orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));*/
		Instant newLastReadAt = request.newLastReadAt();
		ReadStatus readStatus = readStatusRepository.findById(readStatusId)
			.orElseThrow(
				() -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
		readStatus.update(newLastReadAt);
		return readStatusRepository.save(readStatus);
	}

	/**
	 * 읽음 상태를 삭제합니다.
	 * @param id 삭제할 읽음 상태 ID
	 * @throws IllegalArgumentException 읽음 상태가 존재하지 않는 경우
	 */
	@Override
	public void delete(UUID id) {
		// 읽음 상태 존재 여부 확인 후 삭제
		ReadStatus status = readStatusRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));
		readStatusRepository.deleteById(id);
	}

}
