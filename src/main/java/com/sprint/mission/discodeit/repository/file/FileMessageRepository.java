package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class FileMessageRepository  implements MessageRepository {
    private static final String FILE_PATH = "/Users/parkjihyun/Desktop/CodeitProjects/Codeit/1-sprint-mission/files/messages.ser";
    private List<Message> messages;

    public FileMessageRepository() {
        this.messages = loadFromFile();
    }

    @Override
    public void save(Message message) {
        messages.add(message);
        saveToFile();
    }

    @Override
    public Message findByMessageId(UUID messageId) {
        for (Message message : messages) {
            if (message.getId().equals(messageId)) {
                return message;
            }
        }
        return null;
    }

    @Override
    public List<Message> findbyUsername(String username) { // 수정: 메서드 이름 대소문자 일치
        List<Message> result = new ArrayList<>();
        for (Message message : messages) {
            if (message.getUsername().equals(username)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> findAll() {
        return messages;
    }

    @Override
    public void deleteById(UUID messageId) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().equals(messageId)) {
                messages.remove(i);
                saveToFile();
                return;
            }
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Message> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Message>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
