package com.sprint.mission.discodeit.channel.service;

import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.dto.request.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.channel.dto.request.readStatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.channel.entity.ReadStatus;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.channel.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;

public class BasicReadStatusService implements ReadStatusService {
	private final ReadStatusRepository readStatusRepository;
	private final UserRepository userRepository;
	private final ChannelRepository channelRepository;
	private final MessageRepository messageRepository;

	public BasicReadStatusService(ReadStatusRepository readStatusRepository, UserRepository userRepository,
		ChannelRepository channelRepository, MessageRepository messageRepository) {
		this.readStatusRepository = readStatusRepository;
		this.userRepository = userRepository;
		this.channelRepository = channelRepository;
		this.messageRepository = messageRepository;
	}

	/**
	 * 새로운 읽음 상태를 생성합니다.
	 * @param request 읽음 상태 생성 요청 정보 (사용자 ID, 채널 ID, 메시지 ID, 마지막 읽은 시각)
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
		/*if (readStatusRepository.findByUserIdAndChannelId(
			request.userId(), request.channelId()).isPresent()) {
			throw new IllegalArgumentException("ReadStatus already exists");
		}*/

		// messageId가 null이 아닐 때만 메시지 존재 여부 확인
		if (request.messageId() != null) {
			messageRepository.findById(request.messageId())
				.orElseThrow(() -> new IllegalArgumentException("Message not found"));
		}

		// 새로운 읽음 상태 생성 및 저장
		ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId(), request.messageId(),
			request.lastReadAt());
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
	public ReadStatus update(UUID readStatusId, UpdateReadStatusRequest request) {
		// 읽음 상태 조회 및 업데이트
		/*ReadStatus readStatus = readStatusRepository.findByUserIdAndChannelId(request.userId(), request.channelId())
			.orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));*/
		ReadStatus readStatus = readStatusRepository.findById(readStatusId)
			.orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));

		messageRepository.findById(request.messageId())
			.orElseThrow(() -> new IllegalArgumentException("Message not found"));

		readStatus.updateLastReadAt(request.messageId(), request.lastReadAt());
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

	/**
	 * 채널이 삭제될시 readStatus도 같이 삭제
	 * @param channelId
	 */
	@Override
	public void deleteAllByChannelId(UUID channelId) {
		readStatusRepository.deleteAllByChannelId(channelId);
	}
}
