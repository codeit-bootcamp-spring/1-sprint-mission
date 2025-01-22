package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private Map<UUID, Channel> data = new HashMap<>();
    private static final String FILE_PATH = "temp/channel-obj.ser";

    private void saveData(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createChannel(Channel channel) {
        data.put(channel.getId(), channel);
        saveData();
    }

    @Override
    public Optional<Channel> findChannel(UUID id) {
        try( ObjectInputStream ois=new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object findObject = ois.readObject();
            Map<UUID,Channel> readData= (Map<UUID,Channel>) findObject;
            return Optional.ofNullable(readData.get(id));
        } catch (FileNotFoundException e) {
            System.out.println("지정된 파일 없음" + e.getMessage());
            return Optional.empty();
        } catch (IOException |ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Channel> findAllChannels() {
        List<Channel> lists=new ArrayList<>();
        for (UUID uuid : data.keySet()) {
            lists.add(data.get(uuid));
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("temp/all-files-obj.ser"));){
            oos.writeObject(lists);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("temp/all-files-obj.ser"));){
            Object findObject=ois.readObject();
            return (List<Channel>) findObject;
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateChannelName(UUID id, String channelName) {
        if(data.containsKey(id)){
           data.get(id).updateChannelName(channelName);
        }
        saveData();
    }
    @Override
    public void updateChannelDescription(UUID id, String channelContent) {
        if(data.containsKey(id)){
           data.get(id).updateDescription(channelContent);
        }
        saveData();
    }

    @Override
    public void deleteChannel(UUID id) {
        if(data.containsKey(id)){
            data.remove(id);
        }
        saveData();
    }
}
