package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();
    private final String filePath = "messages.dat";

    public FileMessageService() {
        loadFromFile();
    }

    @Override
    public void create(Message message) {
        data.put(message.getId(), message);
        saveToFile();
    }

    @Override
    public Optional<Message> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, Message message) {
        if (data.containsKey(id)) {
            data.put(id, message);
            saveToFile();
        } else {
            throw new IllegalArgumentException("Message not found: " + id);
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
        saveToFile();
    }

    // 데이터를 파일에 저장
    private void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일에서 데이터를 불러오기
    private void loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            data.putAll((Map<UUID, Message>) in.readObject());
        } catch (FileNotFoundException e) {
            System.out.println("Message data file not found, creating a new one.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
