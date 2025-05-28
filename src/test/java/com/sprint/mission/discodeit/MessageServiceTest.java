package com.sprint.mission.discodeit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

	@InjectMocks
	private BasicMessageService messageService;

	private UUID channelId;
	private UUID authorId;
	private UUID messageId;
	private Channel channel;
	private User author;
	private Message message;

	@BeforeEach
	void setUp() {
		channelId = UUID.randomUUID();
		authorId = UUID.randomUUID();
		messageId = UUID.randomUUID();
		channel = mock(Channel.class);
		author = mock(User.class);
		message = mock(Message.class);
	}

	@Test
	void createMessage_Success() {
		MessageCreateRequest request = new MessageCreateRequest("Hello World",channelId, authorId);
		when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
		when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
		when(messageRepository.save(any(Message.class))).thenReturn(message);
		//when(messageMapper.toDto(any(Message.class))).thenReturn(new MessageDto(messageId, null,null,"Hello World",channelId, authorId,  null));

		MessageDto result = messageService.create(request, List.of());

		assertNotNull(result);
		assertEquals("Hello World", result.content());
		verify(messageRepository).save(any(Message.class));
	}

	@Test
	void findMessage_Success() {
		when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));


		MessageDto result = messageService.find(messageId);

		assertNotNull(result);
		assertEquals("Hello World", result.content());
		verify(messageRepository).findById(messageId);
	}

	@Test
	void deleteMessage_Success() {
		when(messageRepository.existsById(messageId)).thenReturn(true);

		messageService.delete(messageId);

		verify(messageRepository).deleteById(messageId);
	}
}
