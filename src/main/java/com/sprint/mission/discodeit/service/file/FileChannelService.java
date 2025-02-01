package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileChannelService implements ChannelService {
    public Path directory = Paths.get(System.getProperty("user.dir"), "data/channel");
    FileManager fileManager = new FileManager();

    //생성
    public Channel createChannel(String channelName, String description) {
        fileManager.init(directory);
        Channel channel = new Channel(channelName, description);
        Path filePath = directory.resolve(channel.getId().toString().concat(".ser"));
        fileManager.save(filePath, channel);
        System.out.println(channel.getChannelName() + " channel created");
        return channel;
    }

    // 수정
    public Channel updateName(Channel channel, String name) {
        List<Channel> channelList = fileManager.allLoad(directory);

        for (Channel targetChannel : channelList) {
            if (targetChannel.getId().equals(channel.getId())) {
                targetChannel.updateChannelName(name);
                fileManager.save(directory.resolve(targetChannel.getId().toString().concat(".ser")), targetChannel);
                System.out.println("channel name updated");
                return targetChannel;
            }
        }
        return null;
    }

    public Channel updateDescription(Channel channel, String description) {
        List<Channel> channelList = fileManager.allLoad(directory);

        for (Channel targetChannel : channelList) {
            if (targetChannel.getId().equals(channel.getId())) {
                targetChannel.updateDescription(description);
                fileManager.save(directory.resolve(targetChannel.getId().toString().concat(".ser")), targetChannel);
                System.out.println("channel description updated");
                return targetChannel;
            }
        }
        return null;
    }

    // 조회
    public Channel findChannel(Channel channel) {
        return fileManager.load(directory.resolve(channel.getId().toString().concat(".ser")), Channel.class);
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
        for (Channel channel : channels) {
            System.out.println(channel);
        }
    }

    // 삭제
    public void deleteChannel(Channel channel) {
        List<Channel> channelList = fileManager.allLoad(directory);

        for (Channel targetChannel : channelList) {
            if (targetChannel.getId().equals(channel.getId())) {
                System.out.println(targetChannel.getChannelName() + " channel deleted");
                fileManager.delete(directory.resolve(targetChannel.getId().toString().concat(".ser")));
            }
        }
    }
}
