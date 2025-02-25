package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Repository
@ConditionalOnProperty(name="discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileChannelRepository implements ChannelRepository {

    private final Path directory;
    private final String extension;

    public FileChannelRepository(RepositoryProperties properties) {
        this.directory = Paths.get(System.getProperty(properties.getBaseDirectory()))
                .resolve(properties.getFileDirectory())
                .resolve("channel");
        this.extension = properties.getExtension();
        FileService.init(this.directory);
    }

    @Override
    public Channel save(Channel channel) {
        Path channelPath = directory.resolve(channel.getId().concat(extension));
        FileService.save(channelPath, channel);
        return channel;
    }

    @Override
    public boolean delete(Channel channel) {
        return FileService.delete(directory.resolve(channel.getId().concat(extension)));
    }

    @Override
    public Channel findById(String id) {
        Path channelPath = directory.resolve(id.concat(extension));
        return (Channel) FileService.read(channelPath);
    }

    @Override
    public List<Channel> findAll() {
        return FileService.load(directory);
    }
}
