package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
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

    // 폴더 주소
    private final Path CHANNELS_PATH;
    private final String EXTENSION = ".ser";

    public FileChannelRepository(){
        this.CHANNELS_PATH = Paths.get(System.getProperty("user.dir"), "file-data-map", "crs",Channel.class.getSimpleName());

        if(Files.notExists(CHANNELS_PATH)){
            try{
                Files.createDirectories(CHANNELS_PATH);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID id) {return CHANNELS_PATH.resolve(id + EXTENSION);}

    public Channel saveFile(Path path, Channel channel){
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return channel;
    }

    public Channel loadFile(Path path){
        Channel channelNullable = null;
        if(Files.exists(path)){
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                channelNullable = (Channel) ois.readObject();
            } catch(IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        return channelNullable;
    }

    @Override
    public Channel saveChannel(Channel channel){
        return saveFile(resolvePath(channel.getId()), channel);
    }

    @Override// 읽기
    public Optional<Channel> findChannelById(UUID id){
        return Optional.ofNullable(loadFile(resolvePath(id)));
    }

    @Override
    public Collection<Channel> getAllChannels(){
        try{
            return Files.list(CHANNELS_PATH)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    // 람다 표현식 -> 메서드 참조
                    .map(this::loadFile)
                    .toList();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    // 삭제
    @Override
    public void deleteChannelById(UUID id){
        Path path = resolvePath(id);
        try{
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
