package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {

    private static final String FILE_PATH = "messageData.ser";
    private Map<UUID, Message> data;

    public FileMessageRepository() {
        this.data = loadDataFromFile();
    }

    // 데이터 파일 읽기
    @SuppressWarnings("unchecked")
    private Map<UUID, Message> loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Map<UUID, Message>) ois.readObject();
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


    public UUID save(Message message) {
        data.put(message.getId(), message);
        saveDataToFile();
        return message.getId();
    }

    public Message findOne(UUID id) {
        return data.get(id);
    }

    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> list = data.values().stream().filter(message -> message.getChannelId().equals(channelId))
                .toList();
        return new ArrayList<>(list);
    }

    public UUID update(Message message){
        data.put(message.getId(), message);
        saveDataToFile();
        return message.getId();
    }

    public UUID delete(UUID id) {
        data.remove(id);
        saveDataToFile();
        return id;
    }
}
