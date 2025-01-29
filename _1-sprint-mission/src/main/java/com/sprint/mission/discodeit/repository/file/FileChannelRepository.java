package com.sprint.mission.discodeit.repository.file;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private final File filePath;
    private final Map<UUID, Channel> channelData;

    public FileChannelRepository(File filePath) {
        this.filePath = filePath;
        this.channelData = loadChannelData();
    }

    // 파일에서 데이터 로드
    private Map<UUID, Channel> loadChannelData() {
        if (!filePath.exists()) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    // 데이터 파일에 저장
    private void saveChannelData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(channelData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Channel create(Channel channel) {
        channelData.put(channel.getId(), channel);
        saveChannelData();
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        return channelData.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelData.values());
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelData.containsKey(channelId)) {
            throw new NoSuchElementException("ChannelId not found");
        }
        channelData.remove(channelId);
        saveChannelData();
    }
}
