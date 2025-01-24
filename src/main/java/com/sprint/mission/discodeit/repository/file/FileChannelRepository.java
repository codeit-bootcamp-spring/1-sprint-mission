package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "channels.dat";
    private List<Channel> cachedChannels;

    public FileChannelRepository() {
        this.cachedChannels = loadChannelsFromFile();
    }

    @Override
    public void createChannel(Channel channel) {
        if (cachedChannels.stream().anyMatch(c -> c.getId().equals(channel.getId()))) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다: " + channel.getId());
        }
        cachedChannels.add(channel);
        saveChannelsToFile();
    }

    @Override
    public Optional<Channel> getChannel(UUID id) {
        return cachedChannels.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(cachedChannels);
    }

    @Override
    public void updateChannel(UUID id, String channelName) {
        Channel channel = getChannel(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 채널을 찾을 수 없습니다: " + id));
        channel.update(channelName);
        saveChannelsToFile();
    }

    @Override
    public void deleteChannel(UUID id) {
        cachedChannels.removeIf(channel -> channel.getId().equals(id));
        saveChannelsToFile();
    }

    private List<Channel> loadChannelsFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object readObject = ois.readObject();
            if (readObject instanceof List<?>) {
                return (List<Channel>) readObject;
            }
            throw new IllegalStateException("파일 데이터가 올바른 형식이 아닙니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("파일에서 채널을 읽지 못했습니다.", e);
        }
    }

    private void saveChannelsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(cachedChannels);
        } catch (IOException e) {
            throw new IllegalStateException("채널을 파일에 저장하지 못했습니다.", e);
        }
    }
}

