package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
@Repository
public class FileChannelRepository implements ChannelRepository {

    private final Path channelDirectory;
    private final String extension = ".ser";;

    public FileChannelRepository(@Value("${discodeit.repository.file-directory}") String fileDirectory) {
        this.channelDirectory = Paths.get(fileDirectory).resolve("channels");
        FileService.init(channelDirectory);
    }

    @Override
    public Channel save(Channel channel) {
        Path channelPath = channelDirectory.resolve(channel.getId().concat(extension));
        FileService.save(channelPath, channel);
        return channel;
    }

    @Override
    public boolean delete(Channel channel) {
        return FileService.delete(channelDirectory.resolve(channel.getId().concat(extension)));
    }

    @Override
    public Channel findById(String id) {
        return findAll().stream().filter(channel -> channel.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Channel> findAll() {
        return FileService.<Channel>load(channelDirectory);
    }
}
