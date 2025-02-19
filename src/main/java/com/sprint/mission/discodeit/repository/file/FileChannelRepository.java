package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ArrayList;

import java.util.UUID;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "files/channels.ser";

    private List<Channel> channels;

    public FileChannelRepository() {
        this.channels = loadFromFile();
    }

    @Override
    public void save(Channel channel) {
        channels.add(channel);
        saveToFile();
    }


    @Override
    public Channel findByChannelname(String channelname) {
        for (Channel channel : channels) {
            if (channel.getChannelName().equals(channelname)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("Channel not found with name: " + channelname);
    }

    @Override
    public Channel findByChannelId(UUID id){
        for (Channel channel : channels) {
            if (channel.getId().equals(id)){
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels);
    }

    @Override
    public void deleteById(UUID id) {
        for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getId().equals(id)) {
                channels.remove(i);
                break;
            }
        }
        saveToFile();
    }


    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Channel> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Channel>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
