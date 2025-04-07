package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.Channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

	@Mock
	private ChannelRepository channelRepository;

	@Mock
	private ReadStatusRepository readStatusRepository;

	@Mock
	private MessageRepository messageRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ChannelMapper channelMapper;

	@InjectMocks
	private BasicChannelService channelService;

	@Test
	public void createPublicChannel_Success() {
		PublicChannelCreateRequest request = new PublicChannelCreateRequest("testChannel", "testDescription");
		Channel channel = new Channel(ChannelType.PUBLIC, "testChannel", "testDescription");
		ChannelDto channelDto = new ChannelDto(channel.getId(), channel.getType(), channel.getName(), channel.getDescription(), null, null);

		when(channelRepository.save(any(Channel.class))).thenReturn(channel);
		when(channelMapper.toDto(any(Channel.class))).thenReturn(channelDto);

		ChannelDto result = channelService.create(request);

		assertThat(result).isNotNull();
		assertThat(result.name()).isEqualTo("testChannel");
		assertThat(result.description()).isEqualTo("testDescription");
		verify(channelRepository).save(any(Channel.class));
		verify(channelMapper).toDto(any(Channel.class));
	}

	@Test
	public void createPrivateChannel_Success() {
		UUID userId1 = UUID.randomUUID();
		UUID userId2 = UUID.randomUUID();
		PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(List.of(userId1, userId2));
		Channel channel = new Channel(ChannelType.PRIVATE, null, null);
		User user1 = new User("user1", "user1@example.com", "password", null);
		User user2 = new User("user2", "user2@example.com", "password", null);
		List<User> ulist = null;
		ulist.add(user1);
		ulist.add(user2);
		Instant now = Instant.now();
		ReadStatus readStatus1 = new ReadStatus(user1, channel, now);
		ReadStatus readStatus2 = new ReadStatus(user2, channel, now);

		when(channelRepository.save(any(Channel.class))).thenReturn(channel);
		when(userRepository.findById(userId1)).thenReturn(Optional.of(user1));
		when(userRepository.findById(userId2)).thenReturn(Optional.of(user2));
		when(readStatusRepository.saveAll(anyList())).thenReturn(List.of(readStatus1, readStatus2));
		when(channelMapper.toDto(any(Channel.class))).thenReturn(new ChannelDto(channel.getId(), channel.getType(), channel.getName(), channel.getDescription(), null, null));

		ChannelDto result = channelService.create(request);

		assertThat(result).isNotNull();
		verify(channelRepository).save(any(Channel.class));
		verify(userRepository).findById(userId1);
		verify(userRepository).findById(userId2);
		verify(readStatusRepository).saveAll(anyList());
		verify(channelMapper).toDto(any(Channel.class));
	}

	@Test
	public void findChannel_Success() {
		UUID channelId = UUID.randomUUID();
		Channel channel = new Channel(ChannelType.PUBLIC, "testChannel", "testDescription");
		ChannelDto channelDto = new ChannelDto(channel.getId(), channel.getType(), channel.getName(), channel.getDescription(), null, null);

		when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
		when(channelMapper.toDto(channel)).thenReturn(channelDto);

		ChannelDto result = channelService.find(channelId);

		assertThat(result).isNotNull();
		assertThat(result.id()).isEqualTo(channelId);
		verify(channelRepository).findById(channelId);
		verify(channelMapper).toDto(channel);
	}

	@Test
	public void findChannel_NotFound() {
		UUID channelId = UUID.randomUUID();

		when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

		assertThrows(ChannelNotFoundException.class, () -> channelService.find(channelId));
		verify(channelRepository).findById(channelId);
	}



	@Test
	public void deleteChannel_Success() {
		UUID channelId = UUID.randomUUID();

		when(channelRepository.existsById(channelId)).thenReturn(true);
		doNothing().when(messageRepository).deleteAllByChannelId(channelId);
		doNothing().when(readStatusRepository).deleteAllByChannelId(channelId);
		doNothing().when(channelRepository).deleteById(channelId);

		channelService.delete(channelId);

		verify(channelRepository).existsById(channelId);
		verify(messageRepository).deleteAllByChannelId(channelId);
		verify(readStatusRepository).deleteAllByChannelId(channelId);
		verify(channelRepository).deleteById(channelId);
	}

	@Test
	public void deleteChannel_NotFound() {
		UUID channelId = UUID.randomUUID();

		when(channelRepository.existsById(channelId)).thenReturn(false);

		assertThrows(ChannelNotFoundException.class, () -> channelService.delete(channelId));
		verify(channelRepository).existsById(channelId);
	}
}