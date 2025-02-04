package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {


    private static final String fileName = "savedata/message.ser";
    private final Map<UUID, Message> messageList;

    public FileMessageRepository() {
        this.messageList = load();
    }

    @Override
    public Message save(Message message) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(messageList);
            return message;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패 : " + e.getMessage(), e);
        }
    }

    @Override
    public Map<UUID, Message> load() {
        File file = new File(fileName);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
        {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드 실패 : " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(UUID id) {
        messageList.remove(id);
    }
}
