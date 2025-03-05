package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FileMessageRepository {
    FileBinaryContentRepository contentRepository;

    @Autowired
    public FileMessageRepository(FileBinaryContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public void createMessage(MessageDto messageDto) throws IOException, ClassNotFoundException {
        Message message = new Message();

        if(checkMessageContains(message.getMessageId())){
            while(!checkMessageContains(message.getMessageId())){
                message.setMessageId(UUID.randomUUID());
            }
        }

        message.setText(messageDto.getText());
        message.setUserId(messageDto.getUserId());
        message.setUserName(messageDto.getUserName());
        message.setChannelId(messageDto.getChannelId());
        message.setChannelName(messageDto.getChannelName());
        message.setIsContainContent(!messageDto.getContentUrl().isEmpty());

        setMessageListToFile(message);
        if(messageDto.getHaveContent()) contentRepository.saveMessageContent(messageDto);
    }

    public void modifyMessage(MessageDto dto) throws IOException, ClassNotFoundException {
        List<Message> messagesList = getMessageFromFile();

        if (messagesList != null) {
            for (Message message : messagesList) {
                if (message.getUserId().equals(dto.getUserId()) && message.getMessageId().equals(dto.getMessageId())) {
                    message.setText(dto.getText());
                    message.setUpdatedAt(Instant.now());
                    modifyListToFile(messagesList);
                    break;
                }
            }
        }
    }

    public List<Message> getMessagesOfChannel(UUID channelId) throws IOException, ClassNotFoundException {
        List<Message> messagesList = getMessageFromFile();
        return messagesList.stream()
                .filter(x -> x.getChannelId().equals(channelId))
                .toList();
    }

    public void deleteMessage(UUID messageId) throws IOException, ClassNotFoundException {
        List<Message> messagesList = getMessageFromFile();

        if(messagesList != null){
            for(Message message : messagesList){
                if(message.getMessageId().equals(messageId)){
                    messagesList.remove(message);
                    contentRepository.findMessageContent(messageId);
                    break;
                }
            }
        }
        modifyListToFile(messagesList);
    }

    public boolean checkMessageContains(UUID id) throws IOException, ClassNotFoundException {
        List<Message> messagesList = getMessageFromFile();

        for(Message message : messagesList){
            if(message.getMessageId().equals(id)){
                return true;
            }
        }

        return false;
    }

    public void modifyUserName(UUID userId, String newUserName) throws IOException, ClassNotFoundException {
        List<Message> messagesList = getMessageFromFile();
        if(messagesList.isEmpty()){
            return;
        }

        for(Message message : messagesList){
            if(message.getUserId().equals(userId)){
                message.setUserName(newUserName);
            }
        }

        modifyListToFile(messagesList);
    }

    public void modifyChannelName(UUID channelId,String newChannelId) throws IOException, ClassNotFoundException {
        List<Message> messagesList = getMessageFromFile();

        if(messagesList.isEmpty()){
            return;
        }

        for(Message message : messagesList){
            if(message.getChannelId().equals(channelId)){
                message.setChannelName(newChannelId);
            }
        }

        modifyListToFile(messagesList);
    }

    public void deletedChannelMessage(UUID channelId) throws IOException, ClassNotFoundException {
        List<Message> messagesList = getMessageFromFile();
        if(messagesList.isEmpty()){
            return;
        }

        messagesList.removeIf(message -> message.getChannelId().equals(channelId));

        modifyListToFile(messagesList);
    }

    public Instant currentMessageOfChannel(UUID channelId) throws IOException, ClassNotFoundException {
        List<Message> messages = getMessageFromFile();
        Instant time = Instant.MIN;

        for(Message message : messages){
            if(message.getChannelId().equals(channelId)){
                if(time.compareTo(message.getUpdatedAt()) <= 0){
                    time = message.getUpdatedAt();
                }
            }
        }
        if(time == Instant.MIN){
            return null;
        }
        return time;
    }

    public Message getMessage(MessageDto message) throws IOException, ClassNotFoundException {
        List<Message> messageList = getMessageFromFile();

        for(Message message1 : messageList){
            if(message1.getMessageId().equals(message.getMessageId())){
                return message1;
            }
        }

        return null;
    }

    public List<Message> getMessages() throws IOException, ClassNotFoundException {
        return getMessageFromFile();
    }

    public List<Message> getMessagesInChannel(MessageDto messageDto) throws IOException, ClassNotFoundException {
        List<Message> messageList = getMessageFromFile();
        List<Message> list = new ArrayList<>();

        for(Message message1 : messageList){
            if(message1.getChannelName().equals(messageDto.getChannelName())){
                list.add(message1);
            }
        }
        return list;
    }



    private void setMessageListToFile(Message message) throws IOException, ClassNotFoundException {
        List<Message> messages = getMessageFromFile();
        messages.add(message);

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Message.ser"));
            out.writeObject(messages);
        }
        catch (IOException ignored) {}
    }

    private void modifyListToFile(List<Message> messages) throws IOException {
        try (
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Message.ser"))) {
                out.writeObject(messages);
        }
    }

    private List<Message> getMessageFromFile() throws IOException, ClassNotFoundException {
        List<Message> messages = new ArrayList<>();


        File file = new File("Message.ser");
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                messages = (List<Message>) in.readObject();
            }
        }

        return messages;
    }
}
