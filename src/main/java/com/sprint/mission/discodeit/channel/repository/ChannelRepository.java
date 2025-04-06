package com.sprint.mission.discodeit.channel.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.entity.Channel;

public interface ChannelRepository {
	Channel save(Channel channel);

	Optional<Channel> findById(UUID id);

	List<Channel> findAll();

	void deleteById(UUID id);

	boolean existsById(UUID id);
}
