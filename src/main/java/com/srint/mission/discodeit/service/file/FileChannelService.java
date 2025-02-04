package com.srint.mission.discodeit.service.file;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {

    private static final String FILE_PATH = "channelData.ser";
    private Map<UUID, Channel> data;

    public FileChannelService() {
        this.data = loadDataFromFile();
    }

    // 데이터 파일 읽기
    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // 데이터 파일 쓰기
    private void saveDataToFile() {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //파일 삭제
    public void deleteFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                System.out.println("파일 삭제에 실패했습니다.");
            }
        }
    }

    // 파일을 빈 파일로 만드는 메서드
    public void clearFile() {
        File file = new File(FILE_PATH);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // 파일을 비우는 방법
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UUID save(Channel channel) {
        data.put(channel.getId(), channel);
        saveDataToFile();
        return channel.getId();
    }

    public Channel findOne(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("조회할 Channel을 찾지 못했습니다.");
        }
        return data.get(id);
    }

    public List<Channel> findAll() {
        if (data.isEmpty()) {
            return Collections.emptyList(); // 빈 리스트 반환
        }
        return new ArrayList<>(data.values());
    }

    public UUID delete(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("삭제할 Channel을 찾지 못했습니다.");
        }
        data.remove(id);
        saveDataToFile();
        return id;
    }

    @Override
    public UUID create(String channelName, User channelOwner) {
        Channel channel = new Channel(channelName, channelOwner);
        return save(channel);
    }

    @Override
    public Channel read(UUID id) {
        return findOne(id);
    }

    @Override
    public List<Channel> readAll() {
        return findAll();
    }

    @Override
    public Channel updateChannelName(UUID id, User user, String channelName) {
        Channel findChannel = findOne(id);
        if (!findChannel.getChannelOwner().userCompare(user)) {
            throw new IllegalStateException("채널 수정 권한이 없습니다.");
        }
        findChannel.setChannelName(channelName);
        save(findChannel);
        return findChannel;
    }

    @Override
    public Channel joinChannel(UUID id, User user) {
        Channel findChannel = findOne(id);
        findChannel.setJoinedUsers(user);
        save(findChannel);
        return findChannel;
    }

    @Override
    public UUID exitChannel(UUID id, User user) {
        Channel findChannel = findOne(id);
        findChannel.deleteJoinedUser(user);
        user.deleteMyChannels(findChannel);
        save(findChannel);
        return findChannel.getId();
    }

    @Override
    public UUID deleteChannel(UUID id, User user) {
        Channel findChannel = findOne(id);
        if (!findChannel.getChannelOwner().userCompare(user)) {
            throw new IllegalStateException("채널 삭제 권한이 없습니다.");
        }
        findChannel.getJoinedUsers().forEach(u -> u.deleteMyChannels(findChannel));
        return  delete(id);
    }

}
