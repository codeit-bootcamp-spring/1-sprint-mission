package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private static final String FILE_PATH = "channels.dat";

    @Override
    public void createChannel(Channel channel) {
        List<Channel> channels = getAllChannels();
        channels.add(channel);
        saveChannels(channels);
    }

    @Override
    public Channel createChannel(ChannelType type, String channelName, String description) {
        Channel channel = new Channel(UUID.randomUUID(), System.currentTimeMillis(), System.currentTimeMillis(), channelName, description);// Repository에 저장
        return channel;
    }

    @Override
    public Channel getChannel(UUID id) {
        List<Channel> channels = getAllChannels();
        for (Channel channel : channels) {
            if (channel.getId().equals(id)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> getAllChannels() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("파일에서 유저를 읽지 못했습니다.", e);
        }
    }

    @Override
    public void updateChannel(UUID id, String channelName) {
        List<Channel> channels = getAllChannels();
        Channel targetChannel = channels.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저를 찾을 수 없습니다: " + id));

        targetChannel.update(channelName); // update 메서드로 updatedAt 갱신 포함
        saveChannels(channels);
    }

    @Override
    public void deleteChannel(UUID id) {
        List<Channel> channels = getAllChannels();
        channels.removeIf(channel -> channel.getId().equals(id));
        saveChannels(channels);
    }

    private void saveChannels(List<Channel> channels) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            throw new IllegalStateException("채널 데이터를 파일에 저장하지 못했습니다.", e);
        }
    }
}
