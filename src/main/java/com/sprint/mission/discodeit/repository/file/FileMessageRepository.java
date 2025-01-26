package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository{
    private static final String FILE_PATH = "temp/messages-obj.dat";
    private final Map<UUID, Message> data=new HashMap<>();

    @Override
    public void createMessage(UUID id, Message message) {
        data.put(id, message);
        save();
    }

    @Override
    public void updateMessage(UUID id, String content) {
        data.get(id).updateMessage(content);
        save();
    }

    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
        save();
    }

    @Override
    public Optional<Message> findById(UUID id) {
        Map<UUID, Message> loadMessages = load();
        return Optional.ofNullable(loadMessages.get(id));
    }

    @Override
    public List<Message> findAll() {
        Map<UUID, Message> loadMessages = load();
        return new ArrayList<>(loadMessages.values());
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        }catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다." + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<UUID, Message> load() {
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (Map<UUID, Message>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다."+e.getMessage());
            return null;
        } catch (IOException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
