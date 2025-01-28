package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {


    private final String fileName = "savedata/channel.ser";
    private final Map<UUID, Channel> channelList;

    public FileChannelService() {
        this.channelList = loadFile();
    }


    public Channel createChannel(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("채널명을 null 또는 빈 문자열로 생성할 수 없습니다.");
        }

        Channel channel = new Channel(name);
        channelList.put(channel.getChannelId(), channel);
        System.out.println("채널명: " + channel.getChannelName() + " 채널이 추가되었습니다.");
        saveFile();
        return channel;
    }



    @Override
    public Channel readChannel(UUID id) {
        Channel channel = channelList.get(id);
        //null 방어
        if (channel == null) {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다. ID: " + id);
        }
        return channel;
    }

    @Override
    public List<Channel> readAllChannel() {
        return new ArrayList<>(channelList.values());
    }

    @Override
    public Channel modifyChannel(UUID id, String name) {
        Channel target = readChannel(id);
        if (target == null) {
            throw new IllegalArgumentException("수정할 채널을 찾을 수 없습니다. ID: " + id);
        }
        String oriName = target.getChannelName();
        target.updateName(name);
        System.out.println("채널 이름 변경: \"" + oriName + "\" -> \"" + name + "\"");
        saveFile();

        return target;
    }

    @Override
    public void deleteChannel(UUID id) {
        Channel target = readChannel(id);
        if (target == null) {
            throw new IllegalArgumentException("삭제할 채널을 찾을 수 없습니다. ID: " + id);
        }
        channelList.remove(id);
        saveFile();

        System.out.println("채널 \"" + target.getChannelName() + "\"이 삭제되었습니다.");
    }


    public void saveFile(){
        File file = new File(fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream
                (new FileOutputStream(fileName))) {
            oos.writeObject(channelList);

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패 : " + e.getMessage(), e);
        }

    }

    public Map<UUID, Channel> loadFile(){

        File file = new File(fileName);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream
                (new FileInputStream(file)))
        {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드 실패 : " + e.getMessage(), e);
        }
    }


}
