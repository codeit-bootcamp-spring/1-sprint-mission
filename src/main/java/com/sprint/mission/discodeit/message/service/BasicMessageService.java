package com.sprint.mission.discodeit.message.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.binaryContent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.binaryContent.entity.BinaryContent;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
	private final MessageRepository messageRepository;
	private final ChannelRepository channelRepository;
	private final UserRepository userRepository;
	private final BinaryContentRepository binaryContentRepository;

	/**
	 * 새로운 메시지를 생성합니다.
	 * @param messageCreateRequest 메시지 생성 요청 정보 (작성자 ID, 채널 ID, 내용, 첨부파일)
	 * @return 생성된 메시지 응답
	 */
	@Override
	public Message create(MessageCreateRequest messageCreateRequest,
		List<BinaryContentCreateRequest> binaryContentCreateRequests) {
		UUID channelId = messageCreateRequest.channelId();
		UUID authorId = messageCreateRequest.authorId();

		if (!channelRepository.existsById(channelId)) {
			throw new NoSuchElementException("Channel with id " + channelId + " does not exist");
		}
		if (!userRepository.existsById(authorId)) {
			throw new NoSuchElementException("Author with id " + authorId + " does not exist");
		}
		//todo 첨부파일 개수 정하기
		//todo binaryContent가 1개로 고정이 되고 save에서 추가가 안된다... 수정예정
		List<UUID> attachmentIds = binaryContentCreateRequests.stream()
			.map(attachmentRequest -> {
				String fileName = attachmentRequest.fileName();
				String contentType = attachmentRequest.contentType();
				byte[] bytes = attachmentRequest.bytes();

				BinaryContent binaryContent = new BinaryContent(bytes, contentType, fileName, (long)bytes.length);
				BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
				return createdBinaryContent.getId();
			})
			.toList();

		String content = messageCreateRequest.content();
		Message message = new Message(content, channelId, authorId, attachmentIds);
		return messageRepository.save(message);
	}

	/**
	 * 메시지 ID로 메시지를 조회합니다.
	 * @param messageId 메시지 ID
	 * @return 메시지 응답
	 */
	@Override
	public Message find(UUID messageId) {
		// 메시지 조회
		Message message = messageRepository.findById(messageId)
			.orElseThrow(() -> new IllegalArgumentException("Message not found"));

		return message;
	}

	/**
	 * 채널 ID로 해당 채널의 모든 메시지를 조회합니다.
	 * @param channelId 채널 ID
	 * @return 메시지 응답 목록
	 */
	@Override
	public List<Message> findAllByChannelId(UUID channelId) {
		List<Message> messages = messageRepository.findAllByChannelId(channelId);

		return messages;
	}

	/**
	 * 메시지를 수정합니다.
	 * @param messageId 수정할 메시지 ID
	 * @param request 메시지 수정 요청 정보 (내용, 새로운 첨부파일)
	 * @return 수정된 메시지 응답
	 */
	@Override
	public Message update(UUID messageId, MessageUpdateRequest request) {
		String newContent = request.newContent();
		Message message = messageRepository.findById(messageId)
			.orElseThrow(
				() -> new NoSuchElementException("Message with id " + messageId + " not found"));
		message.update(newContent);
		return messageRepository.save(message);
	}

	/**
	 * 메시지를 삭제합니다.
	 * @param messageId 삭제할 메시지 ID
	 */
	@Override
	public void delete(UUID messageId) {
		Message message = messageRepository.findById(messageId)
			.orElseThrow(() -> new IllegalArgumentException("Message not found"));

		// 메시지에 연결된 모든 첨부파일 삭제
		message.getAttachmentIds()
			.forEach(binaryContentRepository::deleteById);

		messageRepository.deleteById(messageId);
	}

}