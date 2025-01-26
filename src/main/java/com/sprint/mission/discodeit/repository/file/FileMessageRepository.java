package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileMessageRepository implements MessageRepository {
    private static final String FILE_PATH = "src/main/resources/messages.ser";
    private List<Message> messageData;

    public FileMessageRepository() {
        this.messageData = loadData();
    }

    @Override
    public Message saveMessage(Message message) {
        messageData.add(message);
        saveData();
        return message;
    }

    @Override
    public void deleteMessage(Message message) {
        messageData.remove(message);
        saveData();
    }

    @Override
    public List<Message> printChannel(Channel channel) {
        List<Message> result = new ArrayList<>();
        for (Message message : messageData) {
            if (message.getChannel().equals(channel)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> printByUser(User user) {
        List<Message> result = new ArrayList<>();
        for (Message message : messageData) {
            if (message.getName().equals(user)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> printAllMessage() {
        return new ArrayList<>(messageData);
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messageData);
        } catch (IOException e) {
            System.err.println("===데이터 저장 중 오류 발생: " + e.getMessage() + "===");
        }
    }


    private List<Message> loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("===파일 로드 중 오류 발생: " + e.getMessage() + "===");
            return new ArrayList<>();
        }
    }
}