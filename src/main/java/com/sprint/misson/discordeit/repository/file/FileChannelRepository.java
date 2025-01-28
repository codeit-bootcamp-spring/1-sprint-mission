package com.sprint.misson.discordeit.repository.file;

import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.repository.ChannelRepository;
import com.sprint.misson.discordeit.service.file.FileService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileChannelRepository implements ChannelRepository {

    private final Path channelDirectory;

    public FileChannelRepository() {
        this.channelDirectory = Paths.get(System.getProperty("user.dir")).resolve("data/channel");
        FileService.init(channelDirectory);
    }

    @Override
    public Channel save(Channel channel) {
        Path channelPath = channelDirectory.resolve(channel.getId().concat(".ser"));
        FileService.save(channelPath, channel);
        return channel;
    }

    @Override
    public boolean delete(Channel channel) {
        return FileService.delete(channelDirectory.resolve(channel.getId().concat(".ser")));
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
