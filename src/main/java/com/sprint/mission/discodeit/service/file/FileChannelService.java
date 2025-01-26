package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.*;

public class FileChannelService implements ChannelService {
    private static final String FILE_PATH = "files/channels.ser";
    private List<Channel> channels;

    public FileChannelService() {
        ensureDirectoryExist("files");
        if (channels == null || channels.isEmpty()) {
            this.channels = loadFromFile();
        }
    }

    @Override
    public Channel createChannel(String channelname){
        Channel channel = new Channel(channelname);
        channels.add(channel);
        saveToFile();
        return channel;
    }

    @Override
    public Channel getChannelById(UUID channelId){
        for (Channel channel : channels) {
            if(channel.getId().equals(channelId)){
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> getAllChannels(){
        return new ArrayList<>(channels);
    }

    @Override
    public Channel updateChannelName(UUID channelId, String newName){
        Channel channel = getChannelById(channelId);
        if(channel != null){
            channel.updateChannelName(newName);
            saveToFile();
        }
        return channel;
    }
    @Override
    public void addUserToChannel(UUID channelId, UUID userId) {
        Channel channel = getChannelById(channelId);
        if (channel != null) {
            channel.addUser(userId);
            saveToFile(); // 채널 데이터를 저장
            System.out.println("유저를 채널에 추가했습니다: 유저 ID=" + userId + ", 채널 이름=" + channel.getChannelName());
        } else {
            System.out.println("채널을 찾을 수 없습니다: 채널 ID=" + channelId);
        }
    }

    @Override
    public boolean deleteChannel(UUID channelId){
        for (int i=0; i<channels.size(); i++) {
            if (channels.get(i).getId().equals(channelId)) {
                channels.remove(i);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(channels);
        } catch (IOException e) {
            System.out.println("channel data error"+ e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Channel> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            if (channels == null) {
                System.out.println("채널 데이터 파일이 존재하지 않아 새 리스트를 초기화합니다.");
            }
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("채널 데이터를 파일에서 로드하는 중 오류가 발생했습니다: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    private void ensureDirectoryExist(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("디렉토리가 생성되었습니다: " + directory.getAbsolutePath());
            } else {
                System.err.println("디렉토리 생성에 실패했습니다.");
            }
        }
    }

}
