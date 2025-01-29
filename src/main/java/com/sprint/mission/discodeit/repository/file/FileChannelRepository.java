package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "temp/channels-obj.dat";
    private final Map<UUID, Channel> data=new HashMap<>();

    @Override
    public void createChannel(UUID id, Channel channel) {
        data.put(id, channel);
        save();
    }

    @Override
    public void updateChannelName(UUID id, String name) {
        data.get(id).updateChannelName(name);
        save();
    }

    @Override
    public void updateDescript(UUID id, String descript) {
        data.get(id).updateDescription(descript);
        save();
    }

    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
        save();
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        Map<UUID, Channel> loadChannels = load();
        return Optional.ofNullable(loadChannels.get(id));
    }

    @Override
    public List<Channel> findAll() {
        Map<UUID, Channel> loadChannels =load();
        return new ArrayList<>(loadChannels.values());
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        }catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다." + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<UUID, Channel> load() {
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (Map<UUID, Channel>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다."+e.getMessage());
            return null;
        } catch (IOException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
