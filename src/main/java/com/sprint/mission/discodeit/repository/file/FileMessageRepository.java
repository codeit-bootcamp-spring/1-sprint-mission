package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private static FileMessageRepository instance;

    public static FileMessageRepository getInstance() {
        if (instance == null) {
            instance = new FileMessageRepository();
        }
        return instance;
    }

    @Override
    public void save(List<Message> messageList) {
        try (FileOutputStream fos = new FileOutputStream("Message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (Message message : messageList) {
                oos.writeObject(message);
            }
        } catch (IOException e) {
            System.err.println("Error serializing message: " + e.getMessage());
        }
    }
    @Override
    public List<Message> load() {
        List<Message> messageList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream("Message.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            while (true) {
                try {
                    Message message = (Message) ois.readObject();
                    messageList.add(message);
                } catch (ClassNotFoundException e) {
                    System.err.println("ClassNotFoundException: " + e.getMessage());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

        return messageList;
    }

}
