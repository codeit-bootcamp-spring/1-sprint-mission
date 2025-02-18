package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import jdk.jfr.Registered;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Repository
public class FileMessageRepository implements MessageRepository {

    private final Path DIRECTORY;
    private final Path FILE_PATH;
    private final Map<UUID, Message> data;

    public FileMessageRepository(@Value("${discodeit.repository.file.path}") String path ) {

        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), path, "Message");
        this.FILE_PATH = DIRECTORY.resolve("message.ser"); //두 경로 조합

        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + DIRECTORY, e);
            }
        }

        this.data = loadDataFromFile();
    }

    // 데이터 파일 읽기
    @SuppressWarnings("unchecked")
    private Map<UUID, Message> loadDataFromFile() {
        File file = FILE_PATH.toFile();

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data"+e.getMessage());
        }
    }

    // 데이터 파일에 쓰기
    private void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH.toFile()))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일을 빈 파일로 만드는 메서드
    public void clearFile() {
        File file = FILE_PATH.toFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // 파일을 비우는 방법
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //파일 삭제
    public void deleteFile() {
        File file = FILE_PATH.toFile();
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                System.out.println("파일 삭제에 실패했습니다.");
            }
        }
    }


    @Override
    public UUID save(Message message) {
        data.put(message.getId(), message);
        saveDataToFile();
        return message.getId();
    }

    @Override
    public Message findOne(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> list = data.values().stream().filter(message -> message.getChannelId().equals(channelId))
                .toList();
        return new ArrayList<>(list);
    }

    @Override
    public UUID update(Message message){
        data.put(message.getId(), message);
        saveDataToFile();
        return message.getId();
    }

    @Override
    public UUID delete(UUID id) {
        data.remove(id);
        saveDataToFile();
        return id;
    }

    @Override
    public void deleteByChannelId(UUID channelId){
        findAllByChannelId(channelId)
                .forEach(message -> delete(message.getId()));
    }
}
