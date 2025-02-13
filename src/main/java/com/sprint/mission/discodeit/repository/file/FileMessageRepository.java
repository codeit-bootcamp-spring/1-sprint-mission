package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FileMessageRepository implements MessageRepository {
    /**
     [ ]  JCF 대신 FileIO와 객체 직렬화를 활용해 메소드를 구현하세요.
     JCF 이용하지 않고, 각 도메인 모델 이름(users, chnnels, messages)을 딴
     폴더에 ser파일을 개별 객체로 저장합니다.
     **/

    // 폴더 주소
    private final Path MESSAGES_PATH;
    private final String EXTENSION = ".ser";

    public FileMessageRepository(){
        // 현재 작업하고 있는 폴더/file-data-map/Message
        this.MESSAGES_PATH = Paths.get(System.getProperty("user.dir"), "file-data-map", "crs",Message.class.getSimpleName());
        if(Files.notExists(MESSAGES_PATH)){
            try{
                Files.createDirectories(MESSAGES_PATH);
            } catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }
    private Path resolvePath(UUID id) {
        return MESSAGES_PATH.resolve(id + EXTENSION);
    }


    public Message saveFile(Path path, Message message){
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return message;
    }


    public Message loadFile(Path path){
        Message messageNullable = null;
        if(Files.exists(path)){
            try(FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)) {
                messageNullable = (Message) ois.readObject();
            }catch (IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }
        return messageNullable;
    }


    @Override
    public void saveMessage(Message message) {
        saveFile(resolvePath(message.getId()), message);
    }

    @Override
    public Optional<Message> findMessageById(UUID id) {
        return Optional.ofNullable(loadFile(resolvePath(id)));
    }

    @Override
    public Collection<Message> findAllMessages() {
        try{
            return Files.list(MESSAGES_PATH)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    // 람다 표현식 -> 메서드 참조
                    .map(this::loadFile)
                    .toList();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Message> findMessagesByChannelId(UUID channelId) {
        List<Message> result = new ArrayList<>();
        try( Stream<Path> paths = Files.walk(MESSAGES_PATH)) {
            paths.filter(Files::isRegularFile)
                    .forEach( path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                         ){
                            Message message = (Message) ois.readObject();

                            if(message.getChannelId().equals(channelId)){
                                result.add(message);
                            }

                        }catch(IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    });
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public void deleteMessageById(UUID id) {
        Path path = resolvePath(id);
        try{
            Files.deleteIfExists(path);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
