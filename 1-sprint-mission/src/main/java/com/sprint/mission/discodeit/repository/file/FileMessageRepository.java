package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileMessageRepository implements MessageRepository {
    private static final String FILE_PATH = "tmp/message.ser";

    @Override
    public void save(Message message) {
        List<Message> messageList= loadFromFile();
        if(messageList.stream().anyMatch(message1 -> message1.getMessageUuid().equals(message.getMessageUuid()))){
            throw new IllegalArgumentException("Duplicate Message UUID: " + message.getMessageUuid());
        }
        messageList.add(message);
        saveToFile(messageList);
    }

    @Override
    public Message findByUuid(String messageUuid) {
        return loadFromFile().stream()
                .filter(message -> message.getMessageUuid().equals(messageUuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Message with UUID " + messageUuid + " not found."));
    }

    @Override
    public List<Message> findAll() {
        return loadFromFile();
    }

    @Override
    public void delete(String messageUuid) {
        List<Message>messageList = loadFromFile();
        boolean removed = messageList.removeIf(message -> message.getMessageUuid().equals(messageUuid));
        if(!removed){
            throw new IllegalArgumentException("Message with UUID " + messageUuid + " not found.");
        }
        saveToFile(messageList);
    }


    public List<Message>loadFromFile(){
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {
            return (List<Message>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Initializing empty list.");
            return new ArrayList<>();
        }catch (EOFException e){
            System.out.println("EOFException: File is empty or corrupted. Initializing empty list.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public void saveToFile(List<Message>messageList){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messageList);
        }catch (IOException e){
            throw new RuntimeException("Failed to save messages to file.", e);
        }
    }

}


