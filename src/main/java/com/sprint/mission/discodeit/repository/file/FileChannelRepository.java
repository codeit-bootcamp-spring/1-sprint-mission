package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository {
    private final String filePath;

    public FileChannelRepository(@Value("${file.path.channel}") String filePath) {
        this.filePath = filePath;
    }

    public Channel save(Channel channel) {
        List<Channel> channels = load();
        channels.removeIf(c -> c.getId().equals(channel.getId())); // 기존 채널 제거
        channels.add(channel);
        saveToFile(channels);
        return channel;
    }

    public Optional<Channel> getChannelById(UUID id) {
        return load().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<UUID> getMessagesUUIDFromChannel(UUID uuid) {
        return load().stream()
                .filter(c -> c.getId().equals(uuid))
                .findFirst()
                .map(Channel::getMessageList)
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<Channel> addMessageToChannel(UUID channelUUID, UUID messageUUID) {
        return Optional.empty();
    }

    @Override
    public Optional<Channel> updateChannelName(UUID uuid, String channelName) {
        return Optional.empty();
    }

    @Override
    public void deleteChannel(UUID uuid) {

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

    @Override
    public void save() {

    }

    @Override
    public boolean existsById(UUID uuid) {
        return false;
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
