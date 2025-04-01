package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

	@Mock
	private MessageRepository messageRepository;

	@Mock
	private ChannelRepository channelRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private MessageMapper messageMapper;

	@Mock
	private BinaryContentStorage binaryContentStorage;

	@Mock
	private BinaryContentRepository binaryContentRepository;

	@InjectMocks
	private BasicMessageService messageService;

	@Test
	void 유효한_요청으로_메시지를_생성하면_메시지_DTO를_반환한다() {
		// given
		UUID channelId = UUID.randomUUID();
		UUID authorId = UUID.randomUUID();
		MessageCreateRequest request = new MessageCreateRequest("Hello World", channelId, authorId);

		Channel channel = new Channel(ChannelType.PUBLIC, "ChannelName", "Description");
		User author = new User("author", "author@example.com", "password", null);
		List<BinaryContent> attachments = List.of(
			new BinaryContent("file1.txt", 100L, "text/plain"),
			new BinaryContent("file2.jpg", 200L, "image/jpeg")
		);
		Message message = new Message("Hello World", channel, author, attachments);

		List<BinaryContentDto> attachmentDtos = attachments.stream()
			.map(attachment -> new BinaryContentDto(attachment.getId(), attachment.getFileName(), attachment.getSize(),
				attachment.getContentType()))
			.toList();

		MessageDto messageDto = new MessageDto(
			UUID.randomUUID(),
			Instant.now(),
			null,
			"Hello World",
			channelId,
			new UserDto(authorId, "author", "author@example.com", null, true),
			attachmentDtos
		);

		given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
		given(userRepository.findById(authorId)).willReturn(Optional.of(author));
		given(messageRepository.save(any(Message.class))).willReturn(message);
		given(messageMapper.toDto(message)).willReturn(messageDto);

		// when
		MessageDto result = messageService.create(request, List.of());

		// then
		assertThat(result).isNotNull();
		assertThat(result.content()).isEqualTo("Hello World");
		assertThat(result.channelId()).isEqualTo(channelId);
		assertThat(result.author().id()).isEqualTo(authorId);
		assertThat(result.attachments()).hasSize(2);
	}

	@Test
	void 존재하지_않는_채널_ID로_메시지를_생성하면_ChannelNotFoundException을_던진다() {
		// given
		UUID channelId = UUID.randomUUID();
		UUID authorId = UUID.randomUUID();
		MessageCreateRequest request = new MessageCreateRequest("Hello World", channelId, authorId);

		given(channelRepository.findById(channelId)).willReturn(Optional.empty());

		// when & then
		assertThrows(ChannelNotFoundException.class, () -> messageService.create(request, List.of()));
	}

	@Test
	void 유효한_ID로_메시지를_조회하면_메시지_DTO를_반환한다() {
		// given
		UUID messageId = UUID.randomUUID();

		Channel channel = new Channel(ChannelType.PUBLIC, "ChannelName", "Description");
		User author = new User("author", "author@example.com", "password", null);

		List<BinaryContent> attachments = List.of(
			new BinaryContent("file1.txt", 100L, "text/plain"),
			new BinaryContent("file2.jpg", 200L, "image/jpeg")
		);

		Message message = new Message("Hello World", channel, author, attachments);

		List<BinaryContentDto> attachmentDtos = attachments.stream()
			.map(attachment -> new BinaryContentDto(attachment.getId(), attachment.getFileName(), attachment.getSize(),
				attachment.getContentType()))
			.toList();

		MessageDto messageDto = new MessageDto(
			messageId,
			Instant.now(),
			null,
			"Hello World",
			channel.getId(),
			new UserDto(author.getId(), author.getUsername(), author.getEmail(), null, true),
			attachmentDtos
		);

		given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
		given(messageMapper.toDto(message)).willReturn(messageDto);

		// when
		MessageDto result = messageService.find(messageId);

		// then
		assertThat(result).isNotNull();
		assertThat(result.content()).isEqualTo("Hello World");
	}

	@Test
	void 존재하지_않는_ID로_메시지를_조회하면_MessageNotFoundException을_던진다() {
		// given
		UUID messageId = UUID.randomUUID();

		given(messageRepository.findById(messageId)).willReturn(Optional.empty());

		// when & then
		assertThrows(MessageNotFoundException.class, () -> messageService.find(messageId));
	}

	@Test
	void 유효한_ID로_메시지를_수정하면_수정된_DTO를_반환한다() {
		// given
		UUID messageId = UUID.randomUUID();

		Channel channel = new Channel(ChannelType.PUBLIC, "ChannelName", "Description");

		User author = new User("author", "author@example.com", "password", null);

		List<BinaryContent> attachments = List.of();

		Message existingMessage = new Message("Old Content", channel, author, attachments);

		MessageUpdateRequest request = new MessageUpdateRequest("Updated Content");

		given(messageRepository.findById(messageId)).willReturn(Optional.of(existingMessage));

		given(messageMapper.toDto(existingMessage)).willAnswer(invocation -> {
			existingMessage.update(request.newContent());
			return new MessageDto(
				messageId,
				Instant.now(),
				Instant.now(),
				existingMessage.getContent(),
				channel.getId(),
				new UserDto(author.getId(), author.getUsername(), author.getEmail(), null, true),
				List.of()
			);
		});

		// when
		MessageDto result = messageService.update(messageId, request);

		// then
		assertThat(result).isNotNull();
		assertThat(result.content()).isEqualTo("Updated Content");
	}
}
