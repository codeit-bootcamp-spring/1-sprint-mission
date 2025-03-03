package com.sprint.mission.discodeit.message.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.message.entity.Message;

public interface MessageRepository {
	Message save(Message message);

	Optional<Message> findById(UUID id);

	List<Message> findAllByChannelId(UUID channelId);

	List<Message> findAll();

	boolean existsById(UUID id);

	void deleteById(UUID id);

	void deleteAllByChannelId(UUID channelId);

}
