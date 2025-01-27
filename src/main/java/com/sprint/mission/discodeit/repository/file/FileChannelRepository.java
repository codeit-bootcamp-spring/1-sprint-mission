package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private final String FILE_PATH="channel.ser";
    private final Map<UUID, Channel> data;

    public FileChannelRepository() {
        this.data = loadDataFromFile();
    }


    @Override
    public void createChannel(Channel channel) {
        data.put(channel.getId(), channel);
        saveDataToFile();
    }

    @Override
    public Optional<Channel> getChannelById(UUID id) {
        Channel channelNullable=this.data.get(id);
        return Optional.ofNullable(Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with " + id + " not found")));
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data.values());
    }


    @Override
    public void updateChannel(UUID id, Channel updatedChannel) {
        Channel existingChannel = data.get(id);
        if (existingChannel != null) {
            existingChannel.update(updatedChannel.getChannel(),updatedChannel.getDescription());
            saveDataToFile();
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        if(!this.data.containsKey(id)){
            throw new NoSuchElementException("Channel with id"+id+" not found");
        }
        data.remove(id);
    }

    // 데이터를 파일에 저장
    private void saveDataToFile() {
        File file = new File(FILE_PATH);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file: " + e.getMessage(), e);
        }
    }

    // 파일에서 데이터를 로드
    private Map<UUID, Channel> loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data from file: " + e.getMessage(), e);
        }
    }
}
