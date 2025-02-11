package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class FileMessageRepository implements MessageRepository {
    private static final String fileName = "savedata/message.ser";
    private final Map<UUID, Message> messageList;

    public FileMessageRepository() {
        this.messageList = load();
    }

    // 현재 messageList를 file에 저장하는 로직.
    private void saveList(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(messageList);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패 : ");
        }
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
    public Message findById(UUID id) {
        return messageList.get(id);
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        List<Message> messageFindByChannelList = messageList.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
        return messageFindByChannelList;
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
        saveList();
    }

    @Override
    public void deleteByChannelId(UUID id) {
        List<Message> toDeleteMessageList = findByChannelId(id);
        for (Message toDeleteMessage : toDeleteMessageList) {
            messageList.remove(id);
        }
    }


}
