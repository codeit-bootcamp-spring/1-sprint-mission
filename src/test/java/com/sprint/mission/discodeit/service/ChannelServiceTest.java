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

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

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
	void 유효한_요청으로_공개_채널을_생성하면_채널_DTO를_반환한다() {
		// given
		PublicChannelCreateRequest request = new PublicChannelCreateRequest("PublicChannel", "Description");
		Channel channel = new Channel(ChannelType.PUBLIC, "PublicChannel", "Description");
		ChannelDto channelDto = new ChannelDto(
			UUID.randomUUID(),
			ChannelType.PUBLIC,
			"PublicChannel",
			"Description",
			List.of(), // 공개 채널은 기본적으로 참여자 없음
			null // 생성 시점에는 메시지가 없으므로 null
		);
		given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

		// when
		ChannelDto result = channelService.create(request);

		// then
		assertThat(result).isNotNull();
		assertThat(result.name()).isEqualTo("PublicChannel");
		assertThat(result.description()).isEqualTo("Description");
		assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);
	}

	@Test
	void 유효한_요청으로_비공개_채널을_생성하면_채널_DTO를_반환한다() {
		// given
		List<UUID> participantIds = List.of(UUID.randomUUID(), UUID.randomUUID());
		PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(participantIds);
		Channel channel = new Channel(ChannelType.PRIVATE, null, null);
		List<User> participants = participantIds.stream()
			.map(id -> new User("username" + id, "email" + id + "@example.com", "password", null))
			.toList();

		List<UserDto> participantDtos = participants.stream()
			.map(user -> new UserDto(user.getId(), user.getUsername(), user.getEmail(), null, true))
			.toList();

		ChannelDto channelDto = new ChannelDto(
			UUID.randomUUID(),
			ChannelType.PRIVATE,
			null,
			null,
			participantDtos,
			null // 메시지가 없으므로 null
		);

		given(userRepository.findAllById(participantIds)).willReturn(
			participantIds.stream().map(id -> new User("username", "email@example.com", "password", null)).toList()
		);
		given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

		// when
		ChannelDto result = channelService.create(request);

		// then
		assertThat(result).isNotNull();
		assertThat(result.type()).isEqualTo(ChannelType.PRIVATE);
	}

	@Test
	void 존재하지_않는_ID로_채널을_조회하면_예외를_던진다() {
		// given
		UUID channelId = UUID.randomUUID();

		given(channelRepository.findById(channelId)).willReturn(Optional.empty());

		// when & then
		assertThrows(ChannelNotFoundException.class, () -> channelService.find(channelId));
	}

	@Test
	void 유효한_ID로_채널을_조회하면_채널_DTO를_반환한다() {
		// given
		UUID channelId = UUID.randomUUID();

		List<User> participants = List.of(
			new User("user1", "user1@example.com", "password1", null),
			new User("user2", "user2@example.com", "password2", null)
		);

		List<UserDto> participantDtos = participants.stream()
			.map(user -> new UserDto(user.getId(), user.getUsername(), user.getEmail(), null, true))
			.toList();

		Instant lastMessageAt = Instant.now();

		Channel channel = new Channel(ChannelType.PUBLIC, "ChannelName", "Description");

		ChannelDto channelDto = new ChannelDto(
			channelId,
			ChannelType.PUBLIC,
			"ChannelName",
			"Description",
			participantDtos,
			lastMessageAt
		);

		given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
		given(channelMapper.toDto(channel)).willReturn(channelDto);

		// when
		ChannelDto result = channelService.find(channelId);

		// then
		assertThat(result).isNotNull();
		assertThat(result.name()).isEqualTo("ChannelName");
		assertThat(result.participants()).hasSize(2); // 참여자 2명 확인
		assertThat(result.lastMessageAt()).isEqualTo(lastMessageAt); // 마지막 메시지 시간 확인
	}

	//Todo유효한 ID 적용 예정
	@Test
	void 유효한_ID로_공개_채널을_수정하면_수정된_DTO를_반환한다() {
		// given
		UUID channelId = UUID.randomUUID();
		PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("NewName", "NewDescription");

		// 기존 채널 데이터
		Channel existingChannel = new Channel(ChannelType.PUBLIC, "OldName", "OldDescription");

		// 참여자와 마지막 메시지 시간 설정
		List<User> participants = List.of(
			new User("user1", "user1@example.com", "password1", null),
			new User("user2", "user2@example.com", "password2", null)
		);

		List<UserDto> participantDtos = participants.stream()
			.map(user -> new UserDto(user.getId(), user.getUsername(), user.getEmail(), null, true))
			.toList();

		Instant lastMessageAt = Instant.now();

		// Stub: 기존 채널 반환
		given(channelRepository.findById(channelId)).willReturn(Optional.of(existingChannel));

		// Stub: 채널 수정 후 DTO 반환
		given(channelMapper.toDto(existingChannel)).willAnswer(invocation -> {
			existingChannel.update(request.newName(), request.newDescription());
			return new ChannelDto(
				channelId,
				existingChannel.getType(),
				existingChannel.getName(),
				existingChannel.getDescription(),
				participantDtos,
				lastMessageAt
			);
		});

		// when
		ChannelDto result = channelService.update(channelId, request);

		// then
		assertThat(result).isNotNull();
		assertThat(result.name()).isEqualTo("NewName");
		assertThat(result.description()).isEqualTo("NewDescription");
		assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);
		assertThat(result.participants()).hasSize(2); // 참여자 2명 확인
		assertThat(result.lastMessageAt()).isEqualTo(lastMessageAt); // 마지막 메시지 시간 확인
	}

	@Test
	void 비공개_채널을_수정하려고하면_PrivateChannelUpdateException을_던진다() {
		// given
		UUID channelId = UUID.randomUUID();
		PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("NewName", "NewDescription");

		Channel privateChannel = new Channel(ChannelType.PRIVATE, null, null);

		given(channelRepository.findById(channelId)).willReturn(Optional.of(privateChannel));

		// when & then
		assertThrows(PrivateChannelUpdateException.class, () -> channelService.update(channelId, request));
	}

	@Test
	void 유효한_ID로_채널을_삭제하면_성공한다() {
		// given
		UUID channelId = UUID.randomUUID();

		given(channelRepository.existsById(channelId)).willReturn(true);

		// when
		channelService.delete(channelId);

		// then
		verify(messageRepository, times(1)).deleteAllByChannelId(channelId);
		verify(readStatusRepository, times(1)).deleteAllByChannelId(channelId);
		verify(channelRepository, times(1)).deleteById(channelId);
	}

	@Test
	void 존재하지_않는_ID로_채널을_삭제하려고하면_ChannelNotFoundException을_던진다() {
		// given
		UUID channelId = UUID.randomUUID();

		given(channelRepository.existsById(channelId)).willReturn(false);

		// when & then
		assertThrows(ChannelNotFoundException.class, () -> channelService.delete(channelId));
	}
}