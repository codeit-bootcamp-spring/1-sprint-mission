package com.sprint.mission.discodeit.message.repository.jcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.repository.MessageRepository;

public class JCFMessageRepository implements MessageRepository {
	private final Map<UUID, Message> messageData = new HashMap<>();

	@Override
	public Message save(Message message) {
		messageData.put(message.getId(), message);
		return message;
	}

	@Override
	public Optional<Message> findById(UUID id) {
		return Optional.ofNullable(messageData.get(id));
	}

	@Override
	public List<Message> findAll() {
		return new ArrayList<>(messageData.values());
	}

	@Override
	public List<Message> findAllByChannelId(UUID channelId) {
		List<Message> channelMessages = new ArrayList<>();
		for (Message message : messageData.values()) {
			if (message.getChannelId().equals(channelId)) {
				channelMessages.add(message);
			}
		}
		return channelMessages;
	}

	@Override
	public Optional<Message> findLatestMessageByChannelId(UUID channelId) {
		List<Message> channelMessages = findAllByChannelId(channelId);
		Message latest = null;
		for (Message message : channelMessages) {
			if (latest == null || message.getCreatedAt().isAfter(latest.getCreatedAt())) {
				latest = message;
			}
		}
		return Optional.ofNullable(latest);
	}

	@Override
	public void delete(UUID id) {
		messageData.remove(id);
	}

	@Override
	public void deleteAllByChannelId(UUID channelId) {
		// 채널 ID가 일치하는 모든 메시지의 키를 수집한 후 삭제.
		List<UUID> idsToRemove = new ArrayList<>();
		for (Message message : messageData.values()) {
			if (message.getChannelId().equals(channelId)) {
				idsToRemove.add(message.getId());
			}
		}
		for (UUID id : idsToRemove) {
			messageData.remove(id);
		}
	}

}
