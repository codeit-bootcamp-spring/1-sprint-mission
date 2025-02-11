package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileMessageRepository {
    Path directory = Paths.get(System.getProperty("user.dir"), "data");
    Path filePath = directory.resolve("messages.ser");

    public void init(){
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                System.out.println("디렉토리 생성 완료");
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage(), e);
            }
        }
    }

    public void save(List<Message> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            throw new RuntimeException("채널 저장 실패: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Message> load() {
        List<Message> messages = new ArrayList<>();

        if (Files.exists(filePath)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
                messages = (List<Message>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("메시지 로드 실패: " + e.getMessage(), e);
            }
        }

        return messages;
    }

}
