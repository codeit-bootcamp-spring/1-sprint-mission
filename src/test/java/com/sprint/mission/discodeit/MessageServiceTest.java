package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

	@Mock
	private MessageRepository messageRepository;

	@Mock
	private ChannelRepository channelRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private MessageMapper messageMapper;

	@Mock
	private BinaryContentRepository binaryContentRepository;

	@InjectMocks
	private BasicMessageService messageService;

	@Test
	public void createMessage_Success() {
		// Given
		User user = new User("johnDoe", "john@example.com", "password123", null);
		Channel channel = new Channel(ChannelType.PUBLIC, "testChannel", "testDescription");
		UUID channelId = channel.getId();
		UUID authorId = user.getId();
		MessageCreateRequest request = new MessageCreateRequest("test content", channelId, authorId);
		List<BinaryContent> attachments = List.of(new BinaryContent("test.txt", 10L, "text/plain"));
		List<BinaryContentCreateRequest> attachmentsRequest = attachments.stream()
			.map(binaryContent -> new BinaryContentCreateRequest(
				binaryContent.getFileName(),
				binaryContent.getContentType(),
				new byte[0] // byte 배열은 필요에 따라 설정
			))
			.toList();
		Message message = new Message(request.content(), channel, user, attachments);
		MessageDto messageDto = new MessageDto(UUID.randomUUID(), Instant.now(), Instant.now(), "test content",
			channelId, null, null);

		given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
		given(userRepository.findById(authorId)).willReturn(Optional.of(user));
		given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(attachments.get(0));
		given(messageRepository.save(any(Message.class))).willReturn(message);
		given(messageMapper.toDto(message)).willReturn(messageDto);

		// When
		MessageDto result = messageService.create(request, attachmentsRequest);

		// Then
		assertThat(result).isNotNull();
		then(channelRepository).should().findById(channelId);
		then(userRepository).should().findById(authorId);
		then(binaryContentRepository).should().save(any(BinaryContent.class));
		then(messageRepository).should().save(any(Message.class));
		then(messageMapper).should().toDto(message);
	}

	@Test
	public void createMessage_ChannelNotFound() {
		// Given
		User user = new User("johnDoe", "john@example.com", "password123", null);
		Channel channel = new Channel(ChannelType.PUBLIC, "testChannel", "testDescription");
		UUID channelId = channel.getId();
		UUID authorId = user.getId();
		MessageCreateRequest request = new MessageCreateRequest("test content", channelId, authorId);
		List<BinaryContentCreateRequest> attachmentsRequest = List.of(
			new BinaryContentCreateRequest("test.txt", "text/plain", "test".getBytes()));

		given(channelRepository.findById(channelId)).willReturn(Optional.empty());

		// When & Then
		assertThrows(ChannelNotFoundException.class, () -> messageService.create(request, attachmentsRequest));
		then(channelRepository).should().findById(channelId);
	}

	@Test //이게 의미가 있는 테스트인지 모르겠음
	public void findMessage_Success() {
		// Given
		UUID messageId = UUID.randomUUID();
		User user = new User("johnDoe", "john@example.com", "password123", null);
		Channel channel = new Channel(ChannelType.PUBLIC, "testChannel", "testDescription");
		Message message = new Message("abc", channel, user, null);

		MessageDto messageDto = new MessageDto(messageId, Instant.now(), Instant.now(), "test content", null, null,
			null);

		given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
		given(messageMapper.toDto(message)).willReturn(messageDto);

		// When
		MessageDto result = messageService.find(messageId);

		// Then
		assertThat(result).isNotNull();
		then(messageRepository).should().findById(messageId);
		then(messageMapper).should().toDto(message);
	}

	@Test
	public void findMessage_NotFound() {
		// Given
		UUID messageId = UUID.randomUUID();

		given(messageRepository.findById(messageId)).willReturn(Optional.empty());

		// When & Then
		assertThrows(DiscodeitException.class, () -> messageService.find(messageId));
		then(messageRepository).should().findById(messageId);
	}

	// @Test
	// public void findAllByChannelId_Success() {
	// 	// Given
	// 	UUID channelId = UUID.randomUUID();
	// 	Instant createdAt = Instant.now();
	// 	Pageable pageable = Pageable.ofSize(10);
	// 	Message message = new Message("test content", null, null, null, null, null);
	// 	MessageDto messageDto = new MessageDto(UUID.randomUUID(), createdAt, createdAt, "test content", null, null, null);
	// 	Slice<Message> slice = new SliceImpl<>(List.of(message), pageable, false);
	// 	PageResponse<MessageDto> pageResponse = new PageResponse<>(List.of(messageDto), createdAt, false);
	//
	// 	given(messageRepository.findAllByChannelIdWithAuthor(channelId, createdAt, pageable)).willReturn(slice);
	// 	given(messageMapper.toDto(message)).willReturn(messageDto);
	// 	given(pageResponseMapper.fromSlice(slice, createdAt)).willReturn(pageResponse);
	//
	// 	// When
	// 	PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, createdAt, pageable);
	//
	// 	// Then
	// 	assertThat(result).isNotNull();
	// 	then(messageRepository).should().findAllByChannelIdWithAuthor(channelId, createdAt, pageable);
	// 	then(messageMapper).should().toDto(message);
	// 	then(pageResponseMapper).should().fromSlice(slice, createdAt);
	// }
	//
	// @Test
	// public void updateMessage_Success() {
	// 	// Given
	// 	UUID messageId = UUID.randomUUID();
	// 	MessageUpdateRequest request = new MessageUpdateRequest("new content");
	// 	Message message = new Message("old content", null, null, null, null, null);
	// 	MessageDto messageDto = new MessageDto(messageId, Instant.now(), Instant.now(), "new content", null, null, null);
	//
	// 	given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
	// 	given(messageMapper.toDto(message)).willReturn(messageDto);
	//
	// 	// When
	// 	MessageDto result = messageService.update(messageId, request);
	//
	// 	// Then
	// 	assertThat(result).isNotNull();
	// 	assertThat(result.content()).isEqualTo("new content");
	// 	then(messageRepository).should().findById(messageId);
	// 	then(messageMapper).should().toDto(message);
	// }

	@Test
	public void deleteMessage_Success() {
		// Given
		UUID messageId = UUID.randomUUID();
		given(messageRepository.existsById(messageId)).willReturn(true);

		// When
		messageService.delete(messageId);

		// Then
		then(messageRepository).should().existsById(messageId);
		then(messageRepository).should().deleteById(messageId);
	}

	@Test
	public void deleteMessage_NotFound() {
		// Given
		UUID messageId = UUID.randomUUID();
		given(messageRepository.existsById(messageId)).willReturn(false);

		// When & Then
		assertThrows(DiscodeitException.class, () -> messageService.delete(messageId));
		then(messageRepository).should().existsById(messageId);
	}
}