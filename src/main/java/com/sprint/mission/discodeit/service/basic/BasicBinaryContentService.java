package com.sprint.mission.discodeit.service.basic;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
	private final BinaryContentRepository binaryContentRepository;
	private final UserRepository userRepository;
	private final MessageRepository messageRepository;

	@Override
	public BinaryContentResponse create(CreateBinaryContentRequest request) {
		// 작성자 존재 확인
		userRepository.findById(request.authorId())
			.orElseThrow(() -> new IllegalArgumentException("Author not found"));

		// 메시지 존재 확인 (메시지 첨부파일인 경우)
		if (request.messageId() != null && request.binaryContentType() == BinaryContentType.MESSAGE_ATTACHMENT) {
			messageRepository.findById(request.messageId())
				.orElseThrow(() -> new IllegalArgumentException("Message not found"));
		}

		BinaryContent binaryContent;
		if (request.binaryContentType() == BinaryContentType.PROFILE_IMAGE) {
			binaryContent = new BinaryContent(
				UUID.randomUUID(),
				Instant.now(),
				request.authorId(),
				request.messageId(),
				request.content(),
				request.contentType(),
				request.fileName(),
				request.fileSize()
			);
		} else {
			binaryContent = new BinaryContent(
				Instant.now(),
				request.authorId(),
				request.messageId(),
				request.content(),
				request.contentType(),
				request.fileName(),
				request.fileSize()
			);
		}

		BinaryContent savedContent = binaryContentRepository.save(binaryContent);
		return createBinaryContentResponse(savedContent);
	}

	@Override
	public BinaryContentResponse find(UUID id) {
		BinaryContent content = binaryContentRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Binary content not found"));
		return createBinaryContentResponse(content);
	}

	@Override
	public List<BinaryContentResponse> findAllByIdIn(List<UUID> ids) {
		return ids.stream()
			.map(id -> binaryContentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Binary content not found: " + id)))
			.map(this::createBinaryContentResponse)
			.collect(Collectors.toList());
	}

	@Override
	public void delete(UUID id) {
		binaryContentRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Binary content not found"));
		binaryContentRepository.deleteById(id);
	}

	private BinaryContentResponse createBinaryContentResponse(BinaryContent content) {
		return new BinaryContentResponse(
			content.getId(),
			content.getCreatedAt(),
			content.getAuthorId(),
			content.getMessageId(),
			content.getContent(),
			content.getContentType(),
			content.getFileName(),
			content.getFileSize(),
			content.getBinaryContentType()
		);
	}
}
