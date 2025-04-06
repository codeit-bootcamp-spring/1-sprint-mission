package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceTest {

	@Mock
	private ChannelRepository channelRepository;
	@Mock
	private ChannelMapper channelMapper;
	@InjectMocks
	private BasicChannelService channelService;

	@Test
	public void 채널_정보가_주어졌을_때_서비스의_생성_메서드를_호출하여_비공개_채널이_생성된다() throws Exception {

		//given
		Channel channel = new Channel(ChannelType.PRIVATE, "비공개채널", "설명");
		given(channelRepository.save(any(Channel.class))).willReturn(channel);
		given(channelMapper.toDto(any(Channel.class)))
			.willReturn(new ChannelDto(UUID.randomUUID(), ChannelType.PRIVATE, "비공개채널", "설명", null, null));

		//when
		PublicChannelCreateRequest request = new PublicChannelCreateRequest("비공개채널", "설명");
		ChannelDto savedChannel = channelService.create(request);

		//then
		assertThat(savedChannel.name()).isEqualTo("비공개채널");
		assertThat(savedChannel.description()).isEqualTo("설명");
	}

	@Test
	public void 채널_정보가_주어졌을_때_서비스의_생성_메서드를_호출하여_공개_채널이_생성된다() throws Exception {

		//given
		Channel channel = new Channel(ChannelType.PUBLIC, "공개채널", "설명");
		given(channelRepository.save(any(Channel.class))).willReturn(channel);
		given(channelMapper.toDto(any(Channel.class)))
			.willReturn(new ChannelDto(UUID.randomUUID(), ChannelType.PUBLIC, "공개채널", "설명", null, null));

		//when
		PublicChannelCreateRequest request = new PublicChannelCreateRequest("공개채널", "설명");
		ChannelDto savedChannel = channelService.create(request);

		//then
		assertThat(savedChannel.name()).isEqualTo("공개채널");
		assertThat(savedChannel.description()).isEqualTo("설명");
	}

	@Test
	public void 이미_존재하는_Id로_인해_서비스의_수정_메서드를_호출하여_채널의_정보가_수정되지_않는다() throws Exception {

		//given
		UUID id = UUID.randomUUID();
		given(channelRepository.findById(id)).willReturn(Optional.empty());

		//when
		PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("공개채널2", "설명2");

		//then
		assertThatThrownBy(() -> channelService.update(id, request))
			.isInstanceOf(NoSuchElementException.class);

		verify(channelRepository, never()).save(any(Channel.class));
	}

	@Test
	public void 채널_정보가_주어졌을_때_서비스의_수정_메서드를_호출하여_채널의_이름_및_설명이_수정된다() throws Exception {

		//given
		UUID id = UUID.randomUUID();
		Channel channel = new Channel(ChannelType.PUBLIC, "공개채널2", "설명2");
		given(channelRepository.findById(id)).willReturn(Optional.of(channel));
		given(channelMapper.toDto(any(Channel.class)))
			.willReturn(new ChannelDto(id, ChannelType.PUBLIC, "공개채널2", "설명2", null, null));

		//when
		PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("공개채널2", "설명2");
		ChannelDto savedChannel = channelService.update(id, request);

		//then
		assertThat(savedChannel.name()).isEqualTo("공개채널2");
		assertThat(savedChannel.description()).isEqualTo("설명2");
	}

}