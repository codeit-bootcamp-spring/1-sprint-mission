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
        if(!data.containsKey(message.getId())){
            data.put(message.getId(), message);
        }
        saveDataToFile();
        return message.getId();
    }

    public Message findOne(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("조회할 Message를 찾지 못했습니다.");
        }
        return data.get(id);
    }

    public List<Message> findAll() {
        if (data.isEmpty()) {
            return Collections.emptyList(); // 빈 리스트 반환
        }
        return new ArrayList<>(data.values());
    }

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
        return save(message);
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
        save(findMessage);
        return findMessage;
    }

    @Override
    public UUID deleteMessage(UUID id, User user) {
        Message findMessage = findOne(id);
        if (!findMessage.getUser().userCompare(user)) {
            throw new IllegalStateException("Message 삭제 권한이 없습니다.");
        }
        findMessage.getChannel().deleteMessage(findMessage);
        return delete(findMessage.getId());
    }
}
