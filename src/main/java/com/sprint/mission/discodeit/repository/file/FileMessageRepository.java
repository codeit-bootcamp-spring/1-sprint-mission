package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    private static final String FILE_PATH = "messages.dat";
    private List<Message> cachedMessages;

    public FileMessageRepository() {
        this.cachedMessages = loadMessagesFromFile();
    }

    @Override
    public void createMessage(Message message) {
        if (cachedMessages.stream().anyMatch(m -> m.getId().equals(message.getId()))) {
            throw new IllegalArgumentException("이미 존재하는 iD입니다: " + message.getId());
        }
        cachedMessages.add(message);
        saveMessagesToFile();
    }

    @Override
    public Optional<Message> getMessage(UUID id) {
        return cachedMessages.stream()
                .filter(message -> message.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(cachedMessages);
    }

    @Override
    public void updateMessage(UUID id, String content) {
        Message message = getMessage(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메시지를 찾을 수 없습니다: " + id));
        message.update(content);
        saveMessagesToFile();
    }

    @Override
    public void deleteMessage(UUID id) {
        cachedMessages.removeIf(message -> message.getId().equals(id));
        saveMessagesToFile();
    }

    private List<Message> loadMessagesFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object readObject = ois.readObject();
            if (readObject instanceof List<?>) {
                return (List<Message>) readObject;
            }
            throw new IllegalStateException("파일 데이터가 올바른 형식이 아닙니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("파일에서 메시지를 읽지 못했습니다.", e);
        }
    }

    private void saveMessagesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(cachedMessages);
        } catch (IOException e) {
            throw new IllegalStateException("메시지를 파일에 저장하지 못했습니다.", e);
        }
    }
}
