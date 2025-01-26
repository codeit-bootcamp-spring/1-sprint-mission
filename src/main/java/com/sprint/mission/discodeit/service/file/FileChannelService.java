package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final FileChannelRepository repository;

    public FileChannelService() {
        this.repository = new FileChannelRepository();
    }

    @Override
    public void addChannel(Channel channel) {
        // 비즈니스 로직: 이름 중복 체크
        if (repository.findAll().stream().anyMatch(c -> c.getName().equals(channel.getName()))) {
            throw new IllegalArgumentException("이미 동일한 이름의 채널이 존재합니다.");
        }
        repository.save(channel);
    }

    @Override
    public Channel getChannel(UUID id) {
        Channel channel = repository.findById(id);
        if (channel == null) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
        return channel;
    }

    @Override
    public List<Channel> getAllChannels() {
        return repository.findAll();
    }

    @Override
    public void updateChannel(UUID id, String newName) {
        Channel channel = repository.findById(id);
        if (channel == null) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
        channel.setName(newName); // 채널 이름 변경
        repository.update(id, channel);
    }

    @Override
    public void deleteChannel(UUID id) {
        if (repository.findById(id) == null) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
        repository.delete(id);
    }
}
