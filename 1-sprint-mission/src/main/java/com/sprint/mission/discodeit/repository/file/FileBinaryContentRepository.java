package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FileBinaryContentRepository {

    public void saveUserContent(UserDto userDto) throws IOException, ClassNotFoundException {
        BinaryContent binaryContent = new BinaryContent();
        binaryContent.setUserId(userDto.getId());
        binaryContent.setCreatedAt(Instant.now());
        binaryContent.setMessageId(null);
        binaryContent.setContentUrl(userDto.getContentUrl());

        saveContent(binaryContent);
    }

    public void saveMessageContent(MessageDto messageDto) throws IOException, ClassNotFoundException {
        BinaryContent binaryContent = new BinaryContent();
        binaryContent.setMessageId(messageDto.getMessageId());
        binaryContent.setCreatedAt(Instant.now());
        binaryContent.setUserId(null);
        binaryContent.setContentUrls(messageDto.getContentUrl());

        saveContent(binaryContent);
    }

    public void deleteUser(UUID id) throws IOException, ClassNotFoundException {
        List<BinaryContent> contents = getContentListFromFile();

        for(BinaryContent content : contents){
            if(content.getUserId() != null){
                if(content.getUserId().equals(id)){
                    contents.remove(content);
                    modifyContentOfFile(contents);
                    return;
                }
            }
        }
    }

    public void deleteMessage(UUID id) throws IOException, ClassNotFoundException {
        List<BinaryContent> contents = getContentListFromFile();

        for(BinaryContent content : contents){
            if(content.getMessageId() == null){
                continue;
            }

            if(content.getMessageId().equals(id)){
                contents.remove(content);
            }
        }
        modifyContentOfFile(contents);
    }

    public BinaryContent findMessageContent(UUID messageId) throws IOException, ClassNotFoundException {
        List<BinaryContent> contents = getContentListFromFile();
        for(BinaryContent content : contents){
            if(content.getMessageId() == null){
                continue;
            }

            if(content.getMessageId().equals(messageId)){
                return content;
            }
        }

        return null;
    }


    public BinaryContent findUserContent(UUID userId) throws IOException, ClassNotFoundException {
        List<BinaryContent> contents = getContentListFromFile();
        for(BinaryContent content : contents){
            if(content.getUserId() == null){
                continue;
            }

            if(content.getUserId().equals(userId)){
                return content;
            }
        }

        return null;
    }

    public List<BinaryContent> findAll() throws IOException, ClassNotFoundException {
        return getContentListFromFile();
    }

    private void saveContent(BinaryContent content) throws IOException, ClassNotFoundException {
        List<BinaryContent> contents = getContentListFromFile();
        contents.add(content);

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Contents.ser"));
            out.writeObject(contents);
            out.close();
        }
        catch (IOException ignored) {}
    }

    private List<BinaryContent> getContentListFromFile() throws IOException, ClassNotFoundException {
        List<BinaryContent> userStatuses = new ArrayList<>();


        File file = new File("Contents.ser");
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                userStatuses = (List<BinaryContent>) in.readObject();
                in.close();
            }
        }

        return userStatuses;
    }

    private void modifyContentOfFile(List<BinaryContent> readStatuses) throws IOException {
        try (
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Contents.ser"))) {
            out.writeObject(readStatuses);
            out.close();
        }
    }
}
