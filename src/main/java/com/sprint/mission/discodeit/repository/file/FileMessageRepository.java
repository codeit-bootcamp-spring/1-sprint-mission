package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.checkerframework.checker.units.qual.A;

import java.io.*;
import java.sql.Array;
import java.util.*;

public class FileMessageRepository implements MessageRepository, FileService<Message> {
    private static final String MESSAGE_SAVE_FILE = "config/message.ser";

    @Override
    public Map<UUID, Message> loadFromFile() {
        File file = new File(MESSAGE_SAVE_FILE);
        if(!file.exists()){
            return new HashMap<>();
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis);){

            Object data = ois.readObject();

            if(data instanceof Map){
                return (Map<UUID, Message>) data;
            } else if (data instanceof List){
                List<Message> messages = (List<Message>) data;

                Map<UUID, Message> messageMap = new HashMap<>();
                for(Message message : messages){
                    messageMap.put(message.getId(), message);
                }
                return messageMap;
            } else{
                throw new IllegalStateException("Unknown data format in file");
            }
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public boolean saveToFile(Map<UUID, Message> data) {
        try (FileOutputStream fos = new FileOutputStream(MESSAGE_SAVE_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(data);
            return true;
        } catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(Message message) {
        Map<UUID, Message> messages = loadFromFile();
        messages.put(message.getId(), message);
        saveToFile(messages);
        return true;
    }

    @Override
    public Message findById(UUID id) {
        return loadFromFile().get(id);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(loadFromFile().values());
    }

    @Override
    public Message modify(UUID id, Message modifiedMessage){
        Map<UUID, Message> messages = loadFromFile();

        if(messages.containsKey(id)){
            Message existingChannel = messages.get(id);

//            existingChannel.setName(modifiedChannel.getName());
//            existingChannel.setDescription(modifiedChannel.getDescription());
//            existingChannel.update();
//            saveToFile(channels);
            return existingChannel;
        }else{
            System.out.println("메시지가 존재하지 않습니다.");
        }
        return null;
    }

    @Override
    public boolean deleteById(UUID id) {
        Map<UUID, Message> messages = loadFromFile();

        if(messages.remove(id) != null){
            saveToFile(messages);
            return true;
        }else{
            System.out.println("유요하지 않은 메시지입니다.");
        }
        return false;
    }

    @Override
    public List<Message> readAll(UUID id) {
        return null;
    }
}
