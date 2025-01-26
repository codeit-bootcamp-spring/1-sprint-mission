package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {
    private final String FILE_PATH = "tmp/channel.ser";
    private Map<UUID, Channel> data;
    private static volatile FileChannelService channelService;

    public FileChannelService() {
        this.data = loadDataFromFile();
    }
    private Map<UUID, Channel> loadDataFromFile() {
        File file = new File(FILE_PATH);
        if(!file.exists()){
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드에 실패했습니다 : " + e.getMessage());
        }
    }

    //싱글톤 패턴 사용
    public static FileChannelService getInstance() {
        if (channelService == null) {
            synchronized (FileChannelService.class) {
                if (channelService == null) {
                    channelService = new FileChannelService();
                }
            }
        }
        return channelService;
    }


    private void saveDataToFile() {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("파일에 데이터 저장 작업을 실패했습니다." + e.getMessage());
        }
    }


    @Override
    public Channel createChannel(String channelName, User owner) {
        if (correctChannelName(channelName)) {
            Channel newChannel = new Channel(channelName, owner);
            data.put(newChannel.getId(), newChannel);
            System.out.println(owner.getUserName() + "님께서 새로운 채널을 생성했습니다!");
            return newChannel;
        }
        return null;
    }
    private boolean correctChannelName(String channelName){
        if (channelName.isBlank()) {
            System.out.println("채널이름이 없습니다! 채널이름을 입력해주세요");
            return  false;
        }
        return true;
    }
    @Override
    public List<Channel> getAllChannelList() {
        return data.values().stream().collect(Collectors.toList());
    }

    @Override
    public Channel searchById(UUID channelId) {
        Channel channel = data.get(channelId);
        if(channel == null) {
            System.out.println("검색하려는 해당 채널이 존재하지 않습니다.");
        }
        return channel;
    }

    @Override
    public void updateChannelName(UUID channelId, String channelName) {
        Channel channel = data.get(channelId);
        if(channel == null) {
            System.out.println("해당 채널이 존재하지 않습니다.");
            return;
        }
        if(correctChannelName(channelName)) {
            channel.setChannelName(channelName);
            saveDataToFile();
            System.out.println("채널 이름이 성공적으로 변경되었습니다.");
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = data.remove(channelId);
        if(channel == null) {
            System.out.println("삭제할 채널이 없습니다.");
        } else  {
            saveDataToFile();
            System.out.println("채널을 성공적으로 삭제했습니다.");
        }

    }

}
