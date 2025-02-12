package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileChannelRepository implements ChannelRepository {

    private final String filePath;
    public FileChannelRepository() {
        this.filePath = "tmp/channel.ser";
    }
    public FileChannelRepository(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public void save(Channel channel) {
        Map<UUID, Channel> channelMap = this.findAll();
        if(channelMap == null) {
            channelMap = new HashMap<>();
        }
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            channelMap.put(channel.getId(), channel);
            oos.writeObject(channelMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Channel findById(UUID channelId) {
        Map<UUID, Channel> channelMap = findAll();
        Optional<Channel> findChannels = channelMap.values().stream().filter(channel -> channel.getId().equals(channelId))
                .findAny();
        return findChannels.orElseThrow(() -> new NoSuchElementException("channelId : " + channelId + "를 찾을 수 없습니다."));
    }

    @Override
    public void delete(UUID channelId) {
        Map<UUID, Channel> channelMap = this.findAll();
        if(channelMap == null || !channelMap.containsKey(channelId)) {
            throw new NoSuchElementException("ChannelId: " + channelId + "를 찾을 수 없습니다.");
        }
        channelMap.remove(channelId);
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(channelMap);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Map<UUID, Channel> findAll() {
        Map<UUID,Channel> channelMap = new HashMap<>();
        try (FileInputStream fis = new FileInputStream("tmp/channel.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            channelMap = (Map<UUID, Channel>) ois.readObject();
        } catch ( EOFException e) {
            return channelMap;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return channelMap;
    }
}