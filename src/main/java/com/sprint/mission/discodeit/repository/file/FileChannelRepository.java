package com.sprint.mission.discodeit.repository.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.FileStorage;
import com.sprint.mission.discodeit.service.file.SerializableFileStorage;

@Repository
public class FileChannelRepository implements ChannelRepository {
	private static final Path ROOT_DIR = Paths.get(System.getProperty("user.dir"), "tmp");
	private static final String CHANNEL_FILE = "channel.ser";
	private final FileStorage<Channel> fileStorage;

	public FileChannelRepository() {
		this.fileStorage = new SerializableFileStorage<>(Channel.class);
		fileStorage.init(ROOT_DIR);
	}

	@Override
	public Channel save(Channel channel) {
		List<Channel> channels = findAll();

		// 기존 채널 업데이트 또는 새 채널 추가
		boolean updated = false;
		for (int i = 0; i < channels.size(); i++) {
			if (channels.get(i).getId().equals(channel.getId())) {
				channels.set(i, channel);
				updated = true;
				break;
			}
		}

		if (!updated) {
			channels.add(channel);
		}

		fileStorage.save(ROOT_DIR.resolve(CHANNEL_FILE), channels);
		return channel;
	}

	@Override
	public Optional<Channel> findById(UUID id) {
		return findAll().stream()
			.filter(channel -> channel.getId().equals(id))
			.findFirst();
	}

	@Override
	public List<Channel> findAll() {
		return fileStorage.load(ROOT_DIR);
	}

	@Override
	public void delete(UUID id) {
		List<Channel> channels = findAll();
		channels.removeIf(channel -> channel.getId().equals(id));
		fileStorage.save(ROOT_DIR.resolve(CHANNEL_FILE), channels);
	}
}

