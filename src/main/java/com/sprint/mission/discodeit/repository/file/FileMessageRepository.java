package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileMessageRepository {
    private final String filePath;

    public FileMessageRepository(@Value("${file.path.message}") String filePath) {
        this.filePath = filePath;
    }

    public synchronized void save(Message message) {
        List<Message> messages = load();
        messages.removeIf(m -> m.getId().equals(message.getId())); // 중복 방지
        messages.add(message);
        saveToFile(messages);
    }

    public synchronized Optional<Message> getMessageById(UUID id) {
        return load().stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    public synchronized void deleteById(UUID id) {
        List<Message> messages = load();
        messages.removeIf(m -> m.getId().equals(id));
        saveToFile(messages);
    }

    public synchronized List<Message> getAllMessages() {
        return load();
    }

    private List<Message> load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Message>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메시지 데이터를 불러오는 데 실패했습니다.", e);
        }
    }

    private void saveToFile(List<Message> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            throw new RuntimeException("메시지 데이터를 저장하는 데 실패했습니다.", e);
        }
    }
}
