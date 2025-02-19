package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileChannelRepository {
    private final String filePath;

    public FileChannelRepository(@Value("${file.path.channel}") String filePath) {
        this.filePath = filePath;
    }

    public void save(Channel channel) {
        List<Channel> channels = load();
        channels.removeIf(c -> c.getId().equals(channel.getId())); // 기존 채널 제거
        channels.add(channel);
        saveToFile(channels);
    }

    public Optional<Channel> getChannelById(UUID id) {
        return load().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public void deleteById(UUID id) {
        List<Channel> channels = load();
        if (channels.removeIf(c -> c.getId().equals(id))) {
            saveToFile(channels);
        }
    }

    public List<Channel> getAllChannels() {
        return load();
    }

    private List<Channel> load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Channel>) ois.readObject();
        }
        catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 데이터를 불러오는 데 실패했습니다.", e);
        }
    }

    private void saveToFile(List<Channel> channels) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            throw new RuntimeException("채널 데이터를 저장하는 데 실패했습니다.", e);
        }
    }
}
