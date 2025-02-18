package com.sprint.mission.discodeit.message.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.message.dto.request.binaryContent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.message.entity.BinaryContent;
import com.sprint.mission.discodeit.message.entity.BinaryContentType;
import com.sprint.mission.discodeit.message.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;

public class BasicBinaryContentService implements BinaryContentService {
	private final BinaryContentRepository binaryContentRepository;
	private final UserRepository userRepository;
	private final MessageRepository messageRepository;

	public BasicBinaryContentService(BinaryContentRepository binaryContentRepository, UserRepository userRepository,
		MessageRepository messageRepository) {
		this.binaryContentRepository = binaryContentRepository;
		this.userRepository = userRepository;
		this.messageRepository = messageRepository;
	}

	/**
	 * 새로운 바이너리 컨텐츠를 생성합니다.
	 * @param request 바이너리 컨텐츠 생성 요청 정보 (작성자 ID, 메시지 ID, 파일, 컨텐츠 타입)
	 * @return 생성된 바이너리 컨텐츠 응답
	 * @throws IllegalArgumentException 작성자나 메시지가 존재하지 않는 경우
	 * @throws RuntimeException 파일 처리 실패 시
	 */
	@Override
	public BinaryContent create(CreateBinaryContentRequest request) {
		// 작성자 존재 여부 확인
		userRepository.findById(request.authorId())
			.orElseThrow(() -> new IllegalArgumentException("Author not found"));

		// 메시지 첨부파일인 경우 메시지 존재 여부 확인
		if (request.messageId() != null && request.binaryContentType() == BinaryContentType.MESSAGE_ATTACHMENT) {
			messageRepository.findById(request.messageId())
				.orElseThrow(() -> new IllegalArgumentException("Message not found"));
		}

		try {
			// 컨텐츠 타입에 따라 프로필 이미지 또는 메시지 첨부파일 생성
			BinaryContent content;
			if (request.binaryContentType() == BinaryContentType.PROFILE_IMAGE) {
				content = BinaryContent.createProfileImage(request.authorId(), request.file());
			} else {
				content = BinaryContent.createMessageAttachment(request.authorId(), request.messageId(),
					request.file());
			}

			BinaryContent savedContent = binaryContentRepository.save(content);
			return savedContent;
		} catch (IOException e) {
			throw new RuntimeException("Failed to process file", e);
		}
	}

	/**
	 * ID로 바이너리 컨텐츠를 조회합니다.
	 * @param id 바이너리 컨텐츠 ID
	 * @return 바이너리 컨텐츠 응답
	 * @throws IllegalArgumentException 바이너리 컨텐츠가 존재하지 않는 경우
	 */
	@Override
	public BinaryContent find(UUID id) {
		BinaryContent content = binaryContentRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Binary content not found"));
		return content;
	}

	/**
	 * ID 목록으로 여러 바이너리 컨텐츠를 조회합니다.
	 * @param ids 바이너리 컨텐츠 ID 목록
	 * @return 바이너리 컨텐츠 응답 목록
	 * @throws IllegalArgumentException 바이너리 컨텐츠가 존재하지 않는 경우
	 */
	@Override
	public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
		List<BinaryContent> responses = new ArrayList<>();

		for (UUID id : ids) {
			BinaryContent content = binaryContentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Binary content not found: " + id));
			responses.add(content);
		}

		return responses;
	}

	/**
	 * 메시지 ID로 해당 메시지의 모든 첨부파일을 조회합니다.
	 * @param messageId 메시지 ID
	 * @return 바이너리 컨텐츠 응답 목록
	 * @throws IllegalArgumentException 메시지가 존재하지 않는 경우
	 */
	@Override
	public List<BinaryContent> findAllByMessageId(UUID messageId) {
		// 메시지 존재 여부 확인
		messageRepository.findById(messageId)
			.orElseThrow(() -> new IllegalArgumentException("Message not found"));

		// 해당 메시지의 모든 첨부파일 조회 및 변환
		List<BinaryContent> contents = binaryContentRepository.findByMessageId(messageId);

		return contents;
	}

	/**
	 * 프로필 사진 변경 또는 회원 탈퇴를 위한 이미지를 삭제합니다
	 * @param authorId 사용자의 id
	 */
	@Override
	public void deleteProfileImageByAuthorId(UUID authorId) {
		//작성자로 저장된 모든 바이너리 컨텐츠 조회
		List<BinaryContent> contents = binaryContentRepository.findByAuthorId(authorId);

		//프로필 이미지 타입인 경우만 삭제
		for (BinaryContent content : contents) {
			if (content.getBinaryContentType() == BinaryContentType.PROFILE_IMAGE) {
				binaryContentRepository.deleteById(content.getId());
			}
		}
	}

	/**
	 * 바이너리 컨텐츠를 삭제합니다.
	 * @param id 삭제할 바이너리 컨텐츠 ID
	 * @throws IllegalArgumentException 바이너리 컨텐츠가 존재하지 않는 경우
	 */
	@Override
	public void delete(UUID id) {
		binaryContentRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Binary content not found"));
		binaryContentRepository.deleteById(id);
	}

}
