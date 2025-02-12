package com.sprint.mission.discodeit.repository.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.FileStorage;
import com.sprint.mission.discodeit.service.basic.SerializableFileStorage;

public class FileChannelRepository implements ChannelRepository {
	private final Path rootDir;
	private static final String CHANNEL_FILE = "channel.ser";
	private final FileStorage<Channel> fileStorage;

	public FileChannelRepository(String fileDirectory) {
		this.rootDir = Paths.get(System.getProperty("user.dir"), fileDirectory);
		this.fileStorage = new SerializableFileStorage<>(Channel.class);
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
	public void delete(UUID id) {
		List<Channel> channels = findAll();
		channels.removeIf(channel -> channel.getId().equals(id));
		fileStorage.save(rootDir.resolve(CHANNEL_FILE), channels);
	}

	@Override
	public List<Channel> findAllPublicChannels() {
		List<Channel> allChannels = findAll();
		List<Channel> publicChannels = new ArrayList<>();
		// 모든 채널 목록을 순회하면서 PUBLIC 채널인 경우 추가합니다.
		for (Channel channel : allChannels) {
			if (channel.getChannelType() == ChannelType.PUBLIC) {
				publicChannels.add(channel);
			}
		}
		return publicChannels;
	}

	@Override
	public List<Channel> findPrivateChannelsByUserId(UUID userId) {
		List<Channel> allChannels = findAll();
		List<Channel> privateChannels = new ArrayList<>();
		// 모든 채널 목록을 순회하면서 채널 타입이 PRIVATE이고, 참여자 목록에 userId가 포함된 경우 추가합니다.
		for (Channel channel : allChannels) {
			if (channel.getChannelType() == ChannelType.PRIVATE) {
				Map<UUID, UserResponse> participants = channel.getParticipants();
				if (participants != null && participants.containsKey(userId)) {
					privateChannels.add(channel);
				}
			}
		}
		return privateChannels;
	}
}

