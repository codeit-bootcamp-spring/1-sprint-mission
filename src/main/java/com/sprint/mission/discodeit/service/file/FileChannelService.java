package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private static final String FILE_PATH = "Channles.data";


    public void FileChannel() {
        File file = new File(FILE_PATH);
        if(!file.exists()) {
            resetFile();
        }
    }
    public void resetFile() {
        File file = new File(FILE_PATH);
        // and 연산자는 앞조건을 보고 그다음 조건을 봄
        if(file.exists() && file.delete()) {
            System.out.println("파일을 초기화했습니다.");
        }
        saveAllChannel(new ArrayList<>());
    }

    private void saveAllChannel(List<Channel> channels) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            throw new RuntimeException("메시지 저장 중 오류 발생",e);
        }
    }
    @SuppressWarnings("unchecked")
    public List<Channel> readAllChannel() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Channel>) ois.readObject();
        } catch( EOFException e) {
            return new ArrayList<>();
        } catch(IOException | ClassNotFoundException e) {
            throw new RuntimeException("사용자 목록 읽기 중 오류 발생",e);
        }
    }

    public void createChannel(Channel channel) {
        List<Channel> channels = readAllChannel();
        if(channels.stream().anyMatch(c -> c.getId().equals(channel.getId()))) {
            System.out.println("이미 존재하는 채널입니다." + channel.getId());
        }
        channels.add(channel);
        saveAllChannel(channels);
        System.out.println("채널이 생성되었습니다." + channel);
    }

    public Channel readChannel(String id) {
        List<Channel> channels = readAllChannel();
        System.out.println("채널 uuid 조회");
        channels.forEach(c -> System.out.println("channel ID : " + c.getId()));
        UUID uuid = UUID.fromString(id);
        System.out.println("검색하려는 uuid : " + uuid);

        return channels.stream()
                .filter(c ->c.getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다" + id));
    }

    public void updateChannel(Channel channel) {
        List<Channel> channels = readAllChannel();
        boolean update = false;
        for(int i = 0; i < channels.size(); i++) {
            if(channels.get(i).getId().equals(channel.getId())) {
                channels.set(i, channel);
                update = true;
                break;
            }
        }
        if(!update) {
            throw new IllegalArgumentException("존재하지않는 채널입니다." + channel.getId());
        }
        saveAllChannel(channels);
        System.out.println("채널이 업데이트 되었습니다." + channel);
    }

    public void deleteChannel(String id) {
        List<Channel> channels = readAllChannel();

        if(!channels.removeIf(c -> c.getId().equals(UUID.fromString(id)))) {
            throw new IllegalArgumentException("존재하지않는 채널입니다."  );
        }
        saveAllChannel(channels);
        System.out.println("채널이 삭제 되었습니다.");
    }

    public void addMember(String channelId, User member) {
        List<Channel> channels = readAllChannel();
        Channel channel = channels.stream()
                .filter(c -> c.getId().equals(UUID.fromString(channelId)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 채널입니다." + channelId));

        channel.addMember(member);
        saveAllChannel(channels);
        System.out.println("멤버가 추가 되었습니다." + member);
    }
    public void removeMember(String channelId, User member) {
        List<Channel> channels = readAllChannel();
        Channel channel = channels.stream()
                .filter(c -> c.getId().equals(UUID.fromString(channelId)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 채널입니다." + channelId));

        channel.removeMember(member);
        saveAllChannel(channels);
        System.out.println("멤버가 제거 되었습니다." + member);
    }


}
