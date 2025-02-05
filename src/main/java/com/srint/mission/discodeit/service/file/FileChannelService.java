package com.srint.mission.discodeit.service.file;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.ChannelType;
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
        return data.get(id);
    }

    public List<Channel> findAll() {
/*        if(data.isEmpty()){
            return Collections.emptyList(); // 빈 리스트 반환
        }*/
        return new ArrayList<>(data.values());
    }

    public UUID update(Channel channel){
        data.put(channel.getId(), channel);
        saveDataToFile();
        return channel.getId();
    }

    public UUID delete(UUID id) {
        data.remove(id);
        saveDataToFile();
        return id;
    }


    //서비스 로직
    @Override
    public UUID create(String name, String description, ChannelType type) {
        if (!Channel.validation(name, description)) {
            throw new IllegalArgumentException("잘못된 형식입니다.");
        }
        Channel channel = new Channel(name, description, type);
        return save(channel);
    }

    @Override
    public Channel read(UUID id) {
        Channel findChannel = findOne(id);
        return Optional.ofNullable(findChannel)
                .orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다."));
    }

    @Override
    public List<Channel> readAll() {
        return findAll();
    }

    @Override
    public Channel updateName(UUID id, String channelName) {
        Channel findChannel = findOne(id);
        findChannel.setName(channelName);
        update(findChannel);
        return findChannel;
    }

    @Override
    public Channel updateDescription(UUID id, String description){
        Channel findChannel = findOne(id);
        findChannel.setDescription(description);
        update(findChannel);
        return findChannel;
    }

    @Override
    public Channel updateType(UUID id, ChannelType type){
        Channel findChannel = findOne(id);
        findChannel.setType(type);
        update(findChannel);
        return findChannel;
    }

    @Override
    public UUID deleteChannel(UUID id) {
        Channel findChannel = findOne(id);
        return delete(findChannel.getId());
    }

}
