package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "src/main/resources/channels.ser";
    private Map<User, List<Channel>> channelData;

    public FileChannelRepository() {
        this.channelData = loadData();
    }

    @Override
    public Channel saveChannel(Channel channel) {
        channelData.computeIfAbsent(channel.getUser(), k -> new ArrayList<>()).add(channel);
        saveData();
        return channel;
    }

    @Override
    public void deleteChannel(Channel channel) {
        List<Channel> userChannels = channelData.get(channel.getUser());
        if (userChannels != null) {
            userChannels.remove(channel);
            if (userChannels.isEmpty()) {
                channelData.remove(channel.getUser());
            }
            saveData();
        }
    }

    @Override
    public List<Channel> printUser(User user) {
        return channelData.getOrDefault(user, new ArrayList<>());
    }

    @Override
    public List<Channel> printAllChannel() {
        List<Channel> allChannels = new ArrayList<>();
        for (List<Channel> userChannels : channelData.values()) {
            allChannels.addAll(userChannels);
        }
        return allChannels;
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channelData);
        } catch (IOException e) {
            System.err.println("===데이터 저장 중 오류 발생: " + e.getMessage() + "===");
        }
    }


    private Map<User, List<Channel>> loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<User, List<Channel>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("===데이터 로드 중 오류 발생: " + e.getMessage() + "===");
            return new HashMap<>();
        }
    }
}