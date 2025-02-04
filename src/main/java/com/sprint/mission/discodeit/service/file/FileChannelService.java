package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileChannelService implements ChannelService {
    private static final String FILE_EXTENSION = ".ser";

    public Path directory = Paths.get(System.getProperty("user.dir"), "data/channel");
    public final FileManager fileManager;

    public FileChannelService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    //생성
    public Channel createChannel(String channelName, String description) {
        fileManager.init(directory);
        Channel channel = new Channel(channelName, description);
        Path filePath = directory.resolve(channel.getId().toString().concat(FILE_EXTENSION));
        fileManager.save(filePath, channel);
        System.out.println(channel.getChannelName() + " channel created");
        return channel;
    }

    // 수정
    public Channel updateName(Channel channel, String name) {
        channel.updateChannelName(name);
        fileManager.save(directory.resolve(channel.getId().toString().concat(FILE_EXTENSION)), channel);
        System.out.println(channel.getChannelName() + " channel name updated");
        return channel;
    }

    public Channel updateDescription(Channel channel, String description) {
        channel.updateDescription(description);
        fileManager.save(directory.resolve(channel.getId().toString().concat(FILE_EXTENSION)), channel);
        System.out.println(channel.getChannelName() + " channel description updated : " + description);
        return channel;
    }

    // 조회
    public Channel findChannel(Channel channel) {
        return fileManager.load(directory.resolve(channel.getId().toString().concat(FILE_EXTENSION)), Channel.class);
    }

    public List<Channel> findAllChannels() {
        List<Channel> channelList = fileManager.allLoad(directory);
        return new ArrayList<>(channelList);
    }

    // 프린트
    public void printChannel(Channel channel) {
        System.out.println(channel);
    }

    public void printAllChannels(List<Channel> channels) {
        channels.forEach(System.out::println);
    }

    // 삭제
    public void deleteChannel(Channel channel) {
        List<Channel> channelList = fileManager.allLoad(directory);

        for (Channel targetChannel : channelList) {
            if (targetChannel.getId().equals(channel.getId())) {
                System.out.println(targetChannel.getChannelName() + " channel deleted");
                fileManager.delete(directory.resolve(targetChannel.getId().toString().concat(FILE_EXTENSION)));
            }
        }
    }
}
