package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {


    /*
    비즈니스 로직: 동일
    저장 로직:
    File*Service는 직렬화/역직렬화를 통해 파일을 관리한다.
    - saveFile()
        현재 데이터를 직렬화하여 .ser 확장자로 로컬에 저장한다.
        또한, 데이터 입력, 변경, 삭제 시 saveFile()을 통해 업데이트한다.
    - loadFile()
        로컬 경로의 파일을 읽어 역직렬화하여 Map<UUID, [class] > 형태로 리턴한다.
        이를 통해 데이터를 프로그램 종료 시에도 저장하고, 재시작시 읽어 사용할 수 있다.

    File*Service 역시 동일하게 메모리에 데이터를 올려 사용하나, 프로그램 종료시 삭제된다.

     */


    private static final String fileName = "savedata/channel.ser";
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



    //저장로직

    private void saveFile(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(channelList);

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패 : " + e.getMessage(), e);
        }

    }

    private Map<UUID, Channel> loadFile(){

        File file = new File(fileName);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
        {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드 실패 : " + e.getMessage(), e);
        }
    }


}
