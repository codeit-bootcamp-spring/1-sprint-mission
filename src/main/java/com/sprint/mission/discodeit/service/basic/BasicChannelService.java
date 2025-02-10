package com.sprint.mission.discodeit.service.basic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
	private final ChannelRepository channelRepository;
	private final UserService userService;

	@Override
	public Channel createChannel(Channel channel) {
		channelRepository.findById(channel.getId())
			.ifPresent(chnl -> {
				throw new IllegalArgumentException("Channel Id already exists: " + chnl.getId());
			});

		Channel savedChannel = channelRepository.save(channel);
		System.out.println(savedChannel.toString());
		return savedChannel;
	}

	@Override
	public void addParticipantToChannel(UUID channelId, UUID userId) {
		Channel existChannel = channelRepository.findById(channelId)
			.orElseThrow(() -> new IllegalArgumentException("Channel does not exist: " + channelId));

		UserResponse existUser = userService.findUser(userId);

		existChannel.addParticipant(userId, existUser);
		channelRepository.save(existChannel);

		System.out.println(existChannel.toString());
		System.out.println(existChannel.getName() + " 채널에 " + existUser.username() + " 유저 추가 완료\n");
	}

	@Override
	public Optional<Channel> readChannel(UUID existChannelId) {
		Optional<Channel> foundChannel = channelRepository.findById(existChannelId);
		if (foundChannel.isPresent()) {
			System.out.println(foundChannel.get().toString());
		}
		return foundChannel;
	}

	@Override
	public List<Channel> readAllChannels() {
		return channelRepository.findAll();
	}

	@Override
	public Channel updateChannel(UUID existChannelId, Channel updateChannel) {
		Channel existChannel = channelRepository.findById(existChannelId)
			.orElseThrow(() -> new IllegalArgumentException("Channel does not exist: " + existChannelId));

		System.out.println("업데이트 이전 채널 = " + existChannel.toString());
		existChannel.updateName(updateChannel.getName());
		existChannel.updateDescription(updateChannel.getDescription());
		existChannel.updateParticipants(updateChannel.getParticipants());
		existChannel.updateMessageList(updateChannel.getMessageList());
		existChannel.updateTime();

		Channel updatedChannel = channelRepository.save(existChannel);
		System.out.println("업데이트 이후 채널 = " + updatedChannel.toString());
		return updatedChannel;
	}

	@Override
	public boolean deleteChannel(UUID channelId) {
		Optional<Channel> channelOptional = channelRepository.findById(channelId);
		if (channelOptional.isEmpty()) {
			return false;
		}
		channelRepository.delete(channelId);
		return true;
	}
}
