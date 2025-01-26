package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.checkerframework.checker.units.qual.C;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository, FileService<Channel> {
    private static final String CHANNEL_SAVE_FILE = "config/channel.ser";

    @Override
    public Map<UUID, Channel> loadFromFile() {
        File file = new File(CHANNEL_SAVE_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object data = ois.readObject();

            if (data instanceof Map) {
                return (Map<UUID, Channel>) data;
            } else if (data instanceof List) {
                List<Channel> channels = (List<Channel>) data;

                Map<UUID, Channel> channelMap = new HashMap<>();
                for (Channel channel : channels) {
                    channelMap.put(channel.getId(), channel);
                }
                return channelMap;
            } else {
                throw new IllegalStateException("Unknown data format in file");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public boolean saveToFile(Map<UUID, Channel> data) {
        try (FileOutputStream fos = new FileOutputStream(CHANNEL_SAVE_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(data);
            return true;
        } catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(Channel channel) {
        Map<UUID, Channel> channels = loadFromFile();
        channels.put(channel.getId(), channel);
        saveToFile(channels);
        return true;
    }

    @Override
    public Channel findById(UUID id) {
        return loadFromFile().get(id);
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(loadFromFile().values());
    }

    @Override
    public Channel modify(UUID id, Channel modifiedChannel) {
        Map<UUID, Channel> channels = loadFromFile();

        if(channels.containsKey(id)){
            Channel existingChannel = channels.get(id);

            existingChannel.setName(modifiedChannel.getName());
            existingChannel.setDescription(modifiedChannel.getDescription());
            existingChannel.update();
            saveToFile(channels);
            return existingChannel;
        }else{
            System.out.println("채널이 존재하지 않습니다.");
        }
        return null;
    }

    @Override
    public boolean deleteById(UUID id) {
        Map<UUID, Channel> channels = loadFromFile();

        if(channels.remove(id) != null) {
            saveToFile(channels);
            return true;
        }else{
            System.out.println("유효하지 않은 ID 입니다.. \n");
        }
        return false;
    }

    @Override
    public Channel ownerChange(UUID id, User Owner) {
        return null;
    }

    @Override
    public boolean memberJoin(UUID id, User user) {
        return false;
    }

    @Override
    public boolean memberWithdrawal(UUID id, User user) {
        return false;
    }
}
