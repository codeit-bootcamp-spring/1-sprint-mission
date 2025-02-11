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

	/**
	 * 새로운 읽음 상태를 생성합니다.
	 * @param request 읽음 상태 생성 요청 정보 (사용자 ID, 채널 ID, 마지막 읽은 시간)
	 * @return 생성된 읽음 상태
	 * @throws IllegalArgumentException 사용자나 채널이 존재하지 않거나, 이미 읽음 상태가 존재하는 경우
	 */
	@Override
	public ReadStatus create(CreateReadStatusRequest request) {
		// 사용자 존재 여부 확인
		userRepository.findById(request.userId())
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		// 채널 존재 여부 확인
		channelRepository.findById(request.channelId())
			.orElseThrow(() -> new IllegalArgumentException("Channel not found"));

		// 읽음 상태 중복 체크
		if (readStatusRepository.findByUserIdAndChannelId(
			request.userId(), request.channelId()).isPresent()) {
			throw new IllegalArgumentException("ReadStatus already exists");
		}

		// 새로운 읽음 상태 생성 및 저장
		ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId(), request.lastReadAt());
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
	 * @param id 읽음 상태 ID
	 * @param request 상태 업데이트 요청 정보 (마지막 읽은 시간)
	 * @return 업데이트된 읽음 상태
	 * @throws IllegalArgumentException 읽음 상태가 존재하지 않는 경우
	 */
	@Override
	public ReadStatus update(UUID id, UpdateReadStatusRequest request) {
		// 읽음 상태 조회 및 업데이트
		ReadStatus readStatus = readStatusRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));

		readStatus.updateLastReadAt(request.lastReadAt());
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
		readStatusRepository.deleteByUserIdAndChannelId(status.getUserId(), status.getChannelId());
	}
}
