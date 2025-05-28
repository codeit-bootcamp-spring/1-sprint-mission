package com.sprint.mission.discodeit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import jakarta.persistence.EntityManager;
@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {
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

	private UUID channelId;
	private UUID authorId;
	private Channel channel;
	private User author;
	private MessageCreateRequest messageCreateRequest;
	private List<BinaryContentCreateRequest> binaryContentCreateRequests;

	@BeforeEach
	void setUp() {
		channelId = UUID.randomUUID();
		authorId = UUID.randomUUID();
		channel = mock(Channel.class);
		author = mock(User.class);

		messageCreateRequest = new MessageCreateRequest("Hello World", channelId, authorId);
		binaryContentCreateRequests = List.of(
			new BinaryContentCreateRequest("file.txt", "text/plain", new byte[] {1, 2, 3})
		);
	}

	@Test
	void createMessage_Success() {
		when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
		when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
		when(binaryContentRepository.save(any(BinaryContent.class))).thenAnswer(
			invocation -> invocation.getArgument(0));
		when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

		MessageDto result = messageService.create(messageCreateRequest, binaryContentCreateRequests);

		assertNotNull(result);
		assertEquals("Hello World", result.content());
		verify(channelRepository).findById(channelId);
		verify(userRepository).findById(authorId);
		verify(messageRepository).save(any(Message.class));
	}

}
