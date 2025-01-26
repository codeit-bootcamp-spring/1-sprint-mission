package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "tmp/channel.ser";
    private Map<UUID, Channel> channelMap;
    public FileChannelRepository() {
        this.channelMap = loadFromFile();
    }
    private Map<UUID,Channel> loadFromFile() {
        channelMap = new HashMap<>();
        File chatFile = new File(FILE_PATH);
        if(!chatFile.exists()) {
            System.out.println("파일이 존재하지 않아 새로 생성합니다.");
            return new HashMap<>();
        }
        if(chatFile.length() == 0) {
            System.out.println("파일이 비어 있으므로 빈 맵을 반환합니다.");
            return  new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(chatFile))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("데이터 로드 중 오류 발생 : "+ e.getMessage());
            return new HashMap<>();
        }
    }
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channelMap);
        } catch (IOException e) {
            System.out.println("파일 저장중 오류 발생: " + e.getMessage());
        }
    }
    @Override
    public Channel createChannel(String channelName, User owner) {
        boolean channelExists = channelMap.values().stream().anyMatch(existingChannel -> existingChannel.getChannelName().equals(channelName));
        if (channelExists) {
            System.out.println("이미 존재하는 채널입니다.");
            return null;
        }
        Channel channel = new Channel(channelName,owner);
        channelMap.put(channel.getId(), channel);
        saveToFile();
        System.out.println("환영합니다, 채널이 생성되었습니다 : " + channelMap);
        return channel;
    }

    @Override
    public List<Channel> getAllChannelList() {
        channelMap = loadFromFile();
        return new ArrayList<>(channelMap.values());
    }

    @Override
    public Channel searchById(UUID channelId) {
        Channel channel = channelMap.get(channelId);
        if(channel == null) {
            System.out.println("해당 채널을 검색할 수 없습니다 : " + channelMap);
        }
        return channel;
    }

    @Override
    public void updateChannelName(UUID channelId, String channelName) {
         Channel channel = channelMap.get(channelId);
         if(channel == null) {
             System.out.println("해당 채널을 검색할 수 없습니다 : " + channelMap);
             return;
         }
         channel.setChannelName(channelName);
         saveToFile();
        System.out.println("채널의 이름이 성공적으로 수정되었습니다 : " + channel.getChannelName());

    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel deleteChannel = channelMap.remove(channelId);
        if(deleteChannel == null) {
            System.out.println("해당 채널을 찾을 수 없습니다." + channelMap);
        } else  {
            saveToFile();
            System.out.println("채널을 성공적으로 삭제했습니다 : " + channelMap);
        }
    }
}
