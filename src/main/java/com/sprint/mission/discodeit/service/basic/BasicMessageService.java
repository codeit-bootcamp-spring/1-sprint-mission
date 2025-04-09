package com.sprint.mission.discodeit.service.basic;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

	private final MessageRepository messageRepository;
	//
	private final ChannelRepository channelRepository;
	private final UserRepository userRepository;
	private final MessageMapper messageMapper;
	private final BinaryContentStorage binaryContentStorage;
	private final BinaryContentRepository binaryContentRepository;
	private final PageResponseMapper pageResponseMapper;

	@Transactional
	@Override
	public MessageDto create(MessageCreateRequest messageCreateRequest,
		List<BinaryContentCreateRequest> binaryContentCreateRequests) {
		log.info("Creating message with request: {}, attachments: {}", messageCreateRequest,
			binaryContentCreateRequests != null ? binaryContentCreateRequests.size() : 0);
		try {
			UUID channelId = messageCreateRequest.channelId();
			UUID authorId = messageCreateRequest.authorId();

			Channel channel = channelRepository.findById(channelId)
				.orElseThrow(() -> {
					log.warn("Channel with id {} does not exist", channelId);
					return new ChannelNotFoundException(Map.of("channelId", channelId));
				});
			User author = userRepository.findById(authorId)
				.orElseThrow(() -> {
					log.warn("Author with id {} does not exist", authorId);
					return new UserNotFoundException(Map.of("userId", authorId));
				});

			List<BinaryContent> attachments = binaryContentCreateRequests.stream()
				.map(attachmentRequest -> {
					String fileName = attachmentRequest.fileName();
					String contentType = attachmentRequest.contentType();
					byte[] bytes = attachmentRequest.bytes();

					BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length,
						contentType);
					binaryContentRepository.save(binaryContent);
					binaryContentStorage.put(binaryContent.getId(), bytes);
					return binaryContent;
				})
				.toList();

			String content = messageCreateRequest.content();
			Message message = new Message(
				content,
				channel,
				author,
				attachments
			);

			messageRepository.save(message);
			MessageDto messageDto = messageMapper.toDto(message);
			log.info("Created message: {}", messageDto);
			return messageDto;
		} catch (Exception e) {
			log.error("Error creating message", e);
			throw e;
		}
	}

	@Transactional(readOnly = true)
	@Override
	public MessageDto find(UUID messageId) {
		log.info("Finding message with id: {}", messageId);
		return messageRepository.findById(messageId)
			.map(messageMapper::toDto)
			.orElseThrow(() -> {
				log.warn("Message with id {} not found", messageId);
				return new MessageNotFoundException(Map.of("messageId", messageId));
			});
	}

	@Transactional(readOnly = true)
	@Override
	public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
		Pageable pageable) {
		log.info("Finding all messages for channel id: {}, createAt: {}, pageable: {}", channelId,
			createAt, pageable);
		Slice<MessageDto> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
				Optional.ofNullable(createAt).orElse(Instant.now()),
				pageable)
			.map(messageMapper::toDto);

		Instant nextCursor = null;
		if (!slice.getContent().isEmpty()) {
			nextCursor = slice.getContent().get(slice.getContent().size() - 1)
				.createdAt();
		}

		PageResponse<MessageDto> pageResponse = pageResponseMapper.fromSlice(slice, nextCursor);
		log.info("Found messages: {}", pageResponse);
		return pageResponse;
	}

	@Transactional
	@Override
	public MessageDto update(UUID messageId, MessageUpdateRequest request) {
		log.info("Updating message with id: {}, request: {}", messageId, request);
		String newContent = request.newContent();
		Message message = messageRepository.findById(messageId)
			.orElseThrow(() -> {
				log.warn("Message with id {} not found", messageId);
				return new MessageNotFoundException(Map.of("messageId", messageId));
			});

		message.update(newContent);
		MessageDto messageDto = messageMapper.toDto(message);
		log.info("Updated message: {}", messageDto);
		return messageDto;
	}

	@Transactional
	@Override
	public void delete(UUID messageId) {
		log.info("Deleting message with id: {}", messageId);
		if (!messageRepository.existsById(messageId)) {
			log.warn("Message with id {} not found", messageId);
			throw new MessageNotFoundException(Map.of("messageId", messageId));
		}

		messageRepository.deleteById(messageId);
		log.info("Deleted message with id: {}", messageId);
	}
}
