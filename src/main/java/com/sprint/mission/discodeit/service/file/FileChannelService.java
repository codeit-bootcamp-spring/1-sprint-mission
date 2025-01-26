package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.collection.Channels;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final String filePath;
    private final Channels channels;

    public FileChannelService(String filePath) {
        this.filePath = filePath;
        this.channels = loadFromFile().orElseGet(Channels::new);
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel newChannel = new Channel(channelName);
        channels.add(newChannel);
        saveToFile();
        return newChannel;
    }

    @Override
    public Optional<Channel> getChannel(UUID uuid) {
        return channels.get(uuid);
    }

    @Override
    public Optional<Channel> addMessageToChannel(UUID channelUUID, UUID messageUUID) {
        Optional<Channel> updatedChannel = channels.addMessageToChannel(channelUUID, messageUUID);
        if (updatedChannel.isPresent()) {
            saveToFile();
        }
        return updatedChannel;
    }

    @Override
    public Optional<Channel> updateChannel(UUID uuid, String channelName) {
        Optional<Channel> updatedChannel = channels.updateName(uuid, channelName);
        if (updatedChannel.isPresent()) {
            saveToFile();
        }
        return updatedChannel;
    }

    @Override
    public Optional<Channel> deleteChannel(UUID uuid) {
        Optional<Channel> removedChannel = channels.remove(uuid);
        if (removedChannel.isPresent()) {
            saveToFile();
        }
        return removedChannel;
    }

    @Override
    public Map<UUID, Channel> getChannels() {
        return channels.asReadOnly();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            throw new RuntimeException("채널을 저장하는데 실패", e);
        }
    }

    private Optional<Channels> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return Optional.of((Channels) ois.readObject());
        } catch (FileNotFoundException e) {
            return Optional.empty();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널을 불러오는데 실패", e);
        }
    }
}
