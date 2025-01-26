package com.sprint.mission.service.file;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.ChannelService;

import javax.print.MultiDocPrintService;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileChannelService implements ChannelService {
    private static final String FILE_PATH = "src/main/resources/channels.ser";

    private Map<User, Channel> channelData;

    public FileChannelService() {
        this.channelData = loadData();
    }

    // 채널 생성
    @Override
    public Channel createChannel(User user, String channelName) {
        Channel newChannel = new Channel(user, channelName);
        if(channelName == null){
            System.out.println("\n**채널명을 입력해주세요.**");
        }
        if(channelName.equals(channelData)){
            System.out.println("\n**중복된 채널명입니다.**");
        }
        channelData.put(user, newChannel);
        saveData();
        System.out.println("\n***채널 생성 성공***");
        return newChannel;
    }

    // 채널명 변경
    @Override
    public void updateChannel(User user, String channelName, String modifiedChannelName) {
        Channel channel = channelData.get(user);
        if (channel == null){
            System.out.println("\n**채널을 찾을 수 없습니다.**");
            return;
        }
        if (channel.getChannelName().equals(modifiedChannelName)){
            System.out.println("\n**중복된 채널명입니다.**");
            return;
        }
        channel.setChannelName(modifiedChannelName);
        saveData();
        System.out.println("\n***채널명 변경 성공***");
    }

    // 전체 채널 조회
    @Override
    public List<Channel> findAllChannelList() {
        System.out.println("\n***채널 전체 조회***");
        List<Channel> channels = new ArrayList<>(channelData.values());
        for (Channel channel : channels) {
            System.out.println(channel);
        }
        return channels;
    }

    // 유저가 속한 채널 조회
    @Override
    public void channelInfo(User user) {
        System.out.println("\n***[채널 목록]***");
        for (Channel channel : channelData.values()) {
            if (channel.getUser().equals(user)) {
                System.out.println(channel);
            }
        }
    }

    // 채널 삭제, 채널을 찾을 수 없을 경우 예외 처리
    @Override
    public void deleteChannel(String channelName) {
        User userToDelete = null;
        for (Map.Entry<User, Channel> entry : channelData.entrySet()) {
            if (entry.getValue().getChannelName().equals(channelName)) {
                userToDelete = entry.getKey();
                break;
            }
        }
        if(userToDelete != null) {
            channelData.remove(userToDelete);
            saveData();
            System.out.println("\n***채널 삭제 성공***");
        } else {
            System.out.println("\n**채널을 찾을 수 없습니다.**");
        }
    }

    // 파일에 데이터 저장
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channelData);
            System.out.println("===데이터가 저장되었습니다: " + FILE_PATH + "===");
        } catch (IOException e) {
            System.err.println("===데이터 저장 중 오류 발생: " + e.getMessage()+ "===");
        }
    }
    @SuppressWarnings("unchecked")
    private Map<User, Channel> loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("===파일이 존재하지 않음. 새로 생성됩니다.====");
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<User, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("===파일 로드 중 오류 발생: " + e.getMessage() + "===");
            return new HashMap<>();
        }
    }
    public void resetData() {
        this.channelData = new HashMap<>();
        saveData();
        System.out.println("===데이터가 초기화되어 빈 상태로 저장되었습니다.===");
    }
}
