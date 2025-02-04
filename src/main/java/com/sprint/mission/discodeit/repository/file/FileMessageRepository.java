package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileMessageRepository implements MessageRepository {
    public Path directory = Paths.get(System.getProperty("user.dir"), "data/messages");

    // 초기화
    public void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException("Directory could not be created. directory : " + directory, e);
            }
        }
    }

    // 저장
    public boolean saveMessage(Message message) {
        init(directory); // 초기화
        Path filePath = directory.resolve(message.getId().toString().concat(".ser"));
        if (Files.exists(filePath)) {
            deleteMessage(message);
        }

        try(
                FileOutputStream fos = new FileOutputStream(filePath.toFile(), false);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(message);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("File could not be saved.", e);
        }
    }

    // 조회
    public Message loadMessage(Message message) {
        Path filePath = directory.resolve(message.getId().toString().concat(".ser"));

        if (Files.exists(filePath)) {
            try (
                    FileInputStream fis = new FileInputStream(filePath.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ) {
                return (Message) ois.readObject();
            }  catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("File could not be loaded.", e);
            }
        } else {
            return null;
        }
    }
    public List<Message> getMessagesByChannel(Message message) {
        if (Files.exists(directory)) {
            try {
                List<Message> list = Files.list(directory)
                        .map(path -> {
                            try (
                                    FileInputStream fis = new FileInputStream(path.toFile());
                                    ObjectInputStream ois = new ObjectInputStream(fis)
                            ) {
                                return (Message) ois.readObject();
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException("File could not be loaded.", e);
                            }
                        })
                        .filter(m -> m.getChannel().getChannelName().equals(message.getChannel().getChannelName()))
                        .collect(Collectors.toList());
                return list;
            } catch (IOException e) {
                throw new RuntimeException("Failed to retrieve file list.", e);
            }
        } else {
            return new ArrayList<>();
        }
    }
    public List<Message> loadAllMessages() {
        if (Files.exists(directory)) {
            try {
                List<Message> list = Files.list(directory)
                        .map(path -> {
                            try (
                                    FileInputStream fis = new FileInputStream(path.toFile());
                                    ObjectInputStream ois = new ObjectInputStream(fis)
                            ) {
                                return (Message) ois.readObject();
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException("File could not be loaded.", e);
                            }
                        })
                        .toList();
                return list;
            } catch (IOException e) {
                throw new RuntimeException("Failed to retrieve file list.", e);
            }
        } else {
            return new ArrayList<>();
        }
    }

    // 삭제
    public boolean deleteMessage(Message message) {
        Path filePath = directory.resolve(message.getId().toString().concat(".ser"));
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException("File could not be deleted.", e);
        }
        return false;
    }

}
