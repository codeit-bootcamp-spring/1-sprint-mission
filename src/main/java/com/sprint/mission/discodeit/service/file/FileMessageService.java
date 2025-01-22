package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private static String FILE_PATH = "temp/message-obj.ser";
    private Map<UUID, Message> data = new HashMap<>();

    private void saveData() {
        try (ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void messageSave(Message message) {
        data.put(message.getId(), message);
        saveData();
    }

    @Override
    public Optional<Message> findMessage(UUID id) {
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            Object readData = ois.readObject();
            return Optional.ofNullable((Message) readData);
        } catch (FileNotFoundException e) {
            System.out.println("지정된 파일 없음"+e.getMessage());
            return Optional.empty();
        } catch (IOException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> findAllMessages() {
        List<Message> lists=new ArrayList<>();
        for (UUID uuid : data.keySet()) {
            lists.add(data.get(uuid));
        }
        try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("temp/all-message-obj.ser"))) {
            oos.writeObject(lists);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream("temp/all-message-obj.ser"))) {
            Object readData=ois.readObject();
            return (List<Message>) readData;
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateMessage(UUID id, String updateMessage) {
        if(data.containsKey(id)) {
           String oldMessage=data.get(id).getContent();
            oldMessage = updateMessage;
        }
        saveData();
    }

    @Override
    public void deleteMessage(UUID id) {
        if (data.containsKey(id)) {
            data.remove(id);
        }
        saveData();
    }
}
