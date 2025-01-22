package com.srint.mission.discodeit.service.file;

import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {


    private static final String FILE_PATH = "messageData.ser";
    private Map<UUID, Message> data;

    public FileMessageService() {
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

    // 데이터 파일 저장
    private void saveDataToFile() {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
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
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("조회할 Message를 찾지 못했습니다.");
        }
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Message가 없습니다.");
        }
        return new ArrayList<>(data.values());
    }

    @Override
    public UUID delete(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("삭제할 Message를 찾지 못했습니다.");
        }
        data.remove(id);
        saveDataToFile();
        return id;
    }

    @Override
    public UUID create(String content, User user, Channel channel) {
        Message message = new Message(content, user, channel);
        message.setMessageChannel();
        UUID id = save(message);
        saveDataToFile();
        return id;
    }

    @Override
    public Message read(UUID id) {
        return findOne(id);
    }

    @Override
    public List<Message> readAll() {
        return findAll();
    }

    @Override
    public Message update(UUID id, String message, User user) {
        Message findMessage = findOne(id);
        if (!findMessage.getUser().userCompare(user)) {
            throw new IllegalStateException("Message 변경 권한이 없습니다.");
        }
        findMessage.setContent(message);
        saveDataToFile();
        return findMessage;
    }

    @Override
    public UUID deleteMessage(UUID id, User user) {
        Message findMessage = findOne(id);
        if (!findMessage.getUser().userCompare(user)) {
            throw new IllegalStateException("Message 삭제 권한이 없습니다.");
        }
        findMessage.getChannel().deleteMessage(findMessage);
        data.remove(id);
        saveDataToFile();
        return id;
    }
}
