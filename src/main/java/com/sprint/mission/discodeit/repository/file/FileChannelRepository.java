package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    private static FileChannelRepository instance;

    public static FileChannelRepository getInstance() {
        if (instance == null) {
            instance = new FileChannelRepository();
        }
        return instance;
    }

    @Override
    public void save(Map<UUID, Channel> channelList) {
        try (FileOutputStream fos = new FileOutputStream("Channel.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (Channel channel : channelList.values()) {
                oos.writeObject(channel);
            }
        } catch (IOException e) {
            System.err.println("Error serializing channels: " + e.getMessage());
        }
    }
    @Override
    public Map<UUID, Channel> load() {
        Map<UUID, Channel> channelList = new HashMap<>();

        try (FileInputStream fis = new FileInputStream("Channel.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            while (true) {
                try {
                    Channel channel = (Channel) ois.readObject();
                    channelList.put(channel.getId(), channel);
                } catch (ClassNotFoundException e) {
                    System.err.println("ClassNotFoundException: " + e.getMessage());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

        return channelList;
    }

}
