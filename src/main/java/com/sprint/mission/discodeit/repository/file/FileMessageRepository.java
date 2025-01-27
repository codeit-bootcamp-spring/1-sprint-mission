package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private final String FILE_PATH="message.ser";
    private final Map<UUID, Message> data;

    public FileMessageRepository() {
        this.data=loadDataFromFile();
    }

    @Override
    public void createMessage(Message message) {
        data.put(message.getId(),message);
        saveDataToFile();
    }

    @Override
    public Optional<Message> getMessageById(UUID id) {
        Message messageNullable=this.data.get(id);
        return Optional.ofNullable(Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Messeage with id " + id + " not found")));
    }

    @Override
    public List<Message> getAllMessage() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateMessage(UUID id, Message updatedMessage) {
        Message existingChannel = data.get(id);
        if (existingChannel != null) {
            existingChannel.update(updatedMessage.getContent());
            saveDataToFile();
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        if(!this.data.containsKey(id)){
            throw new NoSuchElementException("Message with id "+id+" not found");
        }
        data.remove(id);
    }

    // 데이터를 파일에 저장
    private void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file: " + e.getMessage(), e);
        }
    }

    // 파일에서 데이터를 로드
    private Map<UUID, Message> loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data from file: " + e.getMessage(), e);
        }
    }
}
