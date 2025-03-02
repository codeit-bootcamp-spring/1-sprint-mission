package com.sprint.mission.discodeit.channel.repository.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.global.util.FileStorage;
import com.sprint.mission.discodeit.global.util.JsonFileStorage;

public class FileChannelRepository implements ChannelRepository {
	private final Path rootDir;
	private static final String CHANNEL_FILE = "channel.json";
	private final FileStorage<Channel> fileStorage;

	public FileChannelRepository(String fileDirectory) {
		this.rootDir = Paths.get(System.getProperty("user.dir"), fileDirectory);
		this.fileStorage = new JsonFileStorage<>(Channel.class);
		fileStorage.init(rootDir);
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

		fileStorage.save(rootDir.resolve(CHANNEL_FILE), channels);
		return channel;
	}

	@Override
	public Optional<Channel> findById(UUID id) {
		List<Channel> channels = findAll();
		for (Channel channel : channels) {
			if (channel.getId().equals(id)) {
				return Optional.of(channel);
			}
		}
		return Optional.empty();
	}

	@Override
	public List<Channel> findAll() {
		Path filePath = rootDir.resolve(CHANNEL_FILE);
		if (Files.exists(filePath) && Files.isDirectory(filePath)) {
			System.err.println("🚨 오류: channel.ser가 디렉토리로 생성됨. 삭제 후 재생성합니다.");
			try {
				Files.delete(filePath);
			} catch (IOException e) {
				throw new RuntimeException("디렉토리 삭제 실패: " + filePath, e);
			}
		}
		List<Channel> channels = fileStorage.load(filePath);
		return channels;

	}

	@Override
	public void deleteById(UUID id) {
		List<Channel> channels = findAll();
		channels.removeIf(channel -> channel.getId().equals(id));
		fileStorage.save(rootDir.resolve(CHANNEL_FILE), channels);
	}

	@Override
	public boolean existsById(UUID id) {
		return findById(id).isPresent();
	}
}

