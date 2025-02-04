package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    /**
     [ ]  JCF 대신 FileIO와 객체 직렬화를 활용해 메소드를 구현하세요.
     JCF 이용하지 않고, 각 도메인 모델 이름(users, chnnels, messages)을 딴
     폴더에 ser파일을 개별 객체로 저장합니다.
     **/

    // FileUserRepository 는 FileIO save/load 분리를 안했는데, 여기서는 하겠습니다!!

    // 폴더 주소
    private final String CHANNELS_PATH = Paths.get("channels").toString();

    public FileChannelRepository(){
        // 초기화 시 폴더의 존재 유무 확인
        File dir = new File(CHANNELS_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public boolean saveFile(Path filePath, Channel channel){
        try(
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channel);
            return true; // 성공적으로 저장된 경우 true 반환
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Channel loadFile(Path filePath){
        if(Files.exists(filePath)){
            try (FileInputStream fis = new FileInputStream(filePath.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                Channel channel = (Channel) ois.readObject();
                return channel; // 읽은 Channel 객체 반환
            } catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }
        }else {
            return null;
        }
    }

    @Override
    public void saveChannel(Channel channel){
        saveFile(Path.of(CHANNELS_PATH + channel.getId().toString() + ".ser"), channel);
    }
    @Override// 읽기
    public Optional<Channel> findChannelById(UUID id){
        return Optional.ofNullable(loadFile(Path.of(CHANNELS_PATH +  id.toString() + ".ser")));
    }
    @Override
    public Collection<Channel> getAllChannels(){
        //Map<UUID, Channel> channelMap = new HashMap<>();
        List<Channel> channelList = new ArrayList<>();
        File channelDir = new File("channels");
        if(channelDir.exists() && channelDir.isDirectory()){
            File[] files = channelDir.listFiles(); // 모든 파일의 주소 반환
            if(files != null){
                for(File file : files){
                    // 파일이 파일이고, file의 이름 마지막이 ".ser"로 끝난다면
                    if(file.isFile() && file.getName().endsWith(".ser")){
                        // fromString 문자열 -> UUID
                        UUID id = UUID.fromString(file.getName().replace(".ser", ""));
                        //channelMap.put(id, findChannelById(id).orElse(null));
                        channelList.add(findChannelById(id).orElse(null));
                    }
                }
            }
        }
        return channelList.isEmpty() ? null : channelList;
    }
    // 삭제
    @Override
    public void deleteAllChannels(){
        File file = new File(CHANNELS_PATH);
        File[] fileList = file.listFiles();
        for(File fileName : fileList){
            fileName.delete();
        }
        // 추가적으로 구현할 수 있는 로직 : Channel이 message를 가지고 있다면,
        // Channel의 하위 메세지도 삭제하기
        System.out.println(" deleteAllChannels 삭제 완료 ");
    }
    @Override
    public void deleteChannelById(UUID id){
        String fileName = CHANNELS_PATH + id.toString() + ".ser";
        File channelFile = new File(fileName);
        if(channelFile.delete()){
            System.out.println(" deleteChannelById 삭제 완료 ");
        }
        // 추가적으로 구현할 수 있는 로직 : Channel이 message를 가지고 있다면,
        // Channel의 하위 메세지도 삭제하기
    }
}
