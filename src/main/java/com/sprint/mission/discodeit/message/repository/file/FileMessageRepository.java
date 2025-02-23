package com.sprint.mission.discodeit.message.repository.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.global.util.FileStorage;
import com.sprint.mission.discodeit.global.util.JsonFileStorage;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.repository.MessageRepository;

public class FileMessageRepository implements MessageRepository {

	private final Path rootDir;
	private static final String MESSAGE_FILE = "message.json";
	private final FileStorage<Message> fileStorage;

	public FileMessageRepository(String fileDirectory) {
		this.rootDir = Paths.get(System.getProperty("user.dir"), fileDirectory);
		this.fileStorage = new JsonFileStorage<>(Message.class);
		fileStorage.init(rootDir);
	}

	@Override
	public Message save(Message message) {
		List<Message> messages = findAll();
		boolean updated = false;
		// 기존 메시지 업데이트: 동일 id가 있는지 검사
		for (int i = 0; i < messages.size(); i++) {
			if (messages.get(i).getId().equals(message.getId())) {
				messages.set(i, message);
				updated = true;
				break;
			}
		}
		// 업데이트된 메시지가 없으면 새 메시지 추가
		if (!updated) {
			messages.add(message);
		}
		fileStorage.save(rootDir.resolve(MESSAGE_FILE), messages);
		return message;
	}

	@Override
	public Optional<Message> findById(UUID id) {
		List<Message> messages = findAll();
		for (Message m : messages) {
			if (m.getId().equals(id)) {
				return Optional.of(m);
			}
		}
		return Optional.empty();
	}

	@Override
	public List<Message> findAll() {
		return fileStorage.load(rootDir.resolve(MESSAGE_FILE));
	}

	@Override
	public List<Message> findAllByChannelId(UUID channelId) {
		List<Message> allMessages = findAll();
		List<Message> channelMessages = new ArrayList<>();
		for (Message m : allMessages) {
			if (m.getChannelId().equals(channelId)) {
				channelMessages.add(m);
			}
		}
		return channelMessages;
	}

	@Override
	public void delete(UUID id) {
		List<Message> messages = findAll();
		messages.removeIf(m -> m.getId().equals(id));
		fileStorage.save(rootDir.resolve(MESSAGE_FILE), messages);
	}

	/**
	 * 특정 채널에 속한 모든 메시지를 삭제합니다.
	 */
	@Override
	public void deleteAllByChannelId(UUID channelId) {
		List<Message> messages = findAll();
		messages.removeIf(m -> m.getChannelId().equals(channelId));
		fileStorage.save(rootDir.resolve(MESSAGE_FILE), messages);
	}

	/**
	 * 주어진 채널에 대한 메시지 목록 중, 가장 최신의 메시지를 조회합니다.
	 * (시간 비교는 BaseEntity에 정의된 getCreatedAt() 메서드를 사용한다고 가정함)
	 */
	@Override
	public Optional<Message> findLatestMessageByChannelId(UUID channelId) {
		List<Message> channelMessages = findAllByChannelId(channelId);
		Message latest = null;
		for (Message m : channelMessages) {
			if (latest == null || m.getCreatedAt().isAfter(latest.getCreatedAt())) {
				latest = m;
			}
		}
		return Optional.ofNullable(latest);
	}
}
