package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private final String FILE_NAME = "C:\\Users\\ypd06\\codit\\files\\channel.ser";

    private void saveToSer(String fileName, Map<UUID, Channel> channelData) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(channelData); // 직렬화하여 파일에 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, Channel> loadFromSer(String fileName) {
        Map<UUID, Channel> map = new HashMap<>();
        File file = new File(fileName);

        if (!file.exists() || file.length() == 0) {
            // 파일이 없거나 크기가 0이면 빈 Map 반환
            return map;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            map = (Map<UUID, Channel>) ois.readObject(); // 역직렬화
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return map;
    }

    @Override
    public Channel save(String channelName, ChannelType type) {
        Map<UUID, Channel> channelMap = loadFromSer(FILE_NAME);
        if (channelMap.values().stream().anyMatch(channel -> channel.getName().equals(channelName))) {
            System.out.println("이미 존재하는 채널입니다.");
            return channelMap.get(channelMap.keySet().stream().filter(s -> channelMap.get(s).getName().equals(channelName)).findFirst().get()); //존재하는 채널 UUID 반환
        }
        System.out.println("채널 생성 중");
        Channel channel = new Channel(channelName, type);
        channelMap.put(channel.getId(), channel);
        saveToSer(FILE_NAME, channelMap);

        return channel;
    }//public

    @Override
    public Channel save(ChannelType type) {
        Map<UUID, Channel> channelMap = loadFromSer(FILE_NAME);
        System.out.println("채널 생성 중");
        Channel channel = new Channel(type);
        channelMap.put(channel.getId(), channel);
        saveToSer(FILE_NAME, channelMap);
        return channel;
    }//private

    @Override
    public Channel findById(UUID id) {
        Map<UUID, Channel> channelMap = loadFromSer(FILE_NAME);
        if (!channelMap.containsKey(id)) {
            return null;
        }
        return channelMap.get(id);
    }

    @Override
    public List<Channel> findAll() {
        List<Channel> collect = loadFromSer(FILE_NAME).values().stream().toList();
        return new ArrayList<>(collect);
    }

    @Override
    public boolean delete(UUID channelId) {
        Map<UUID, Channel> channelMap = loadFromSer(FILE_NAME);
        if (channelMap.containsKey(channelId)) {
            channelMap.remove(channelId);
            saveToSer(FILE_NAME, channelMap);
            return true;
        } else {
            //System.out.println("채널을 찾을 수 없습니다.");
            return false;
        }
    }

    @Override
    public void update(UUID id, String name) {
        Map<UUID, Channel> channelMap = loadFromSer(FILE_NAME);
        /*
        if (channelMap.values().stream().anyMatch(channel -> channel.getChannelName().equals(name))) {
            System.out.println("이미 존재하는 채널입니다.");
            return;
        }*/
        /*List<Channel> channels = findAll();
        if(channels.contains(findById(id))){
            System.out.println("이미 존재하는 채널입니다.");
            return;
        }*/
        if (channelMap.containsKey(id)) {
            System.out.println("채널 수정 중");
            Channel channel = findById(id);
            channelMap.remove(channel.getId());
            channel.update(name);
            channelMap.put(channel.getId(), channel);
            saveToSer(FILE_NAME, channelMap);

        } else {
            System.out.println("채널을 찾을 수 없습니다.");
        }
        //리턴해서 출력하는 방안 고려
    }


}
