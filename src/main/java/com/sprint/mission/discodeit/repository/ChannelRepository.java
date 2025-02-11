package com.sprint.mission.discodeit.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.Channel;

public interface ChannelRepository {
	Channel save(Channel channel);

	Optional<Channel> findById(UUID id);

	List<Channel> findAll();

	void delete(UUID id);

	List<Channel> findAllPublicChannels();

	List<Channel> findPrivateChannelsByUserId(UUID userId);
}
