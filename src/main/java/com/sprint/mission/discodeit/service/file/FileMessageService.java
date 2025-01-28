package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.MessageValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private static final Path filePath = Paths.get(System.getProperty("user.dir"), "tmp/message.ser");
    private final MessageValidator messageValidator = new MessageValidator();
    private final UserService userService;
    private final ChannelService channelService;

    public FileMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(String content, UUID channelId, UUID writerId) {
        Channel getChannel = channelService.searchById(channelId);
        User getWriter = userService.searchById(writerId);

        if (!Objects.isNull(getChannel) && !Objects.isNull(getWriter)) {
            if (messageValidator.inValidContent(content)) {
                Message newMessage = new Message(getChannel, getWriter, content);
                List<Message> saveMessageList = loadMessageListToFile();
                saveMessageList.add(newMessage);
                saveMessageToFile(saveMessageList);
                System.out.println(getChannel.getTitle() + " channel: " + getWriter.getName() + " send new message");
                return newMessage;
            }
        }
        return null;
    }

    @Override
    public List<Message> getAllMessageList() {
        return loadMessageListToFile();
    }

    @Override
    public Message searchById(UUID messageId){
        for (Message message : loadMessageListToFile()) {
            if (message.getId().equals(messageId)) {
                return message;
            }
        }
        return null;
    }

    @Override
    public void updateMessage(UUID messageId, String content){
        if (!Objects.isNull(searchById(messageId)) && messageValidator.inValidContent(content)) {
            List<Message> saveMessageList = loadMessageListToFile();
            for (Message message : saveMessageList) {
                if (message.getId().equals(messageId)) {
                    message.updateContent(content);
                }
            }
            saveMessageToFile(saveMessageList);
        }
    }

    @Override
    public void deleteMessage(UUID messageId){
        if (!Objects.isNull(searchById(messageId))) {
            List<Message> saveMessageList = new ArrayList<>();
            for (Message message : loadMessageListToFile()) {
                if(!message.getId().equals(messageId)) {
                    saveMessageList.add(message);
                }
            }
            saveMessageToFile(saveMessageList);
        }
    }

    private void saveMessageToFile(List<Message> saveMessageList) {
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile(),false);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            for (Message user : saveMessageList) {
                oos.writeObject(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Message> loadMessageListToFile() {
        List<Message> data = new ArrayList<>();
        if(!Files.exists(filePath)) {
            return data;
        }
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                data.add((Message) ois.readObject());
            }
        } catch (EOFException e) {
//            System.out.println("read all objects");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
