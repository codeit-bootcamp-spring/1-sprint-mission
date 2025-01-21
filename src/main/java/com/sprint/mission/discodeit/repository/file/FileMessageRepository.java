package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private static final Path filePath = Paths.get(System.getProperty("user.dir"), "tmp/message.ser");

    public Message save(Message message) {
        Map<UUID, Message> savedMessageList = loadMessageMapToFile();
        savedMessageList.put(message.getId(), message);
        return message;
    }

    public Optional<Message> findById(UUID messageId) {
        Map<UUID, Message> savedMessageList = loadMessageMapToFile();
        return Optional.ofNullable(savedMessageList.get(messageId));
    }

    public List<Message> findAll() {
        return loadMessageMapToFile().values().stream().toList();
    }

    public void delete(UUID messageId) {
        Map<UUID, Message> savedMessageList = loadMessageMapToFile();
        savedMessageList.remove(messageId);
        saveMessageMapToFile(savedMessageList);
    }

    private void saveMessageMapToFile(Map<UUID, Message> saveMessageMap) {
        List<Message> saveMessageList = saveMessageMap.values().stream().toList();
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile(),false);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            for (Message message : saveMessageList) {
                oos.writeObject(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<UUID, Message> loadMessageMapToFile() {
        Map<UUID, Message> data = new HashMap<>();
        if(!Files.exists(filePath)) {
            return data;
        }
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                Message serMessage = (Message) ois.readObject();
                data.put(serMessage.getId(), serMessage);
            }
        } catch (EOFException e) {
//            System.out.println("read all objects");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
