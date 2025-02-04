package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileMessageService implements MessageService {
    private static final String FILE_EXTENSION = ".ser";

    public Path directory = Paths.get(System.getProperty("user.dir"), "data/messages");
    public final FileManager fileManager;

    public FileMessageService(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    // 생성
    public Message createMessage(String content, Channel channel, User sender) {
        fileManager.init(directory);
        Message message = new Message(content, channel, sender);
        Path filePath = directory.resolve(message.getId().toString().concat(FILE_EXTENSION));
        fileManager.save(filePath, message);
        System.out.println(channel.getChannelName() + " channel " + sender.getUsername() + " : " + content);
        return message;
    }

    // 내용 수정
    public Message updateContent(Message message, String content) {
        List<Message> messageList = fileManager.allLoad(directory);

        for (Message targetMessage : messageList) {
            if (targetMessage.getId().equals(message.getId())) {
                targetMessage.updateContent(content);
                fileManager.save(directory.resolve(targetMessage.getId().toString().concat(FILE_EXTENSION)), targetMessage);
                System.out.println("content updated");
                return targetMessage;
            }
        }
        return null;
    }

    // 조회
    public Message findMessageById(Message message) {
        return fileManager.load(directory.resolve(message.getId().toString().concat(FILE_EXTENSION)), Message.class);
    }
    public List<Message> findMessageByChannel(Message message) {
        List<Message> messageList = fileManager.allLoad(directory);
        List<Message> messageByChannel = new ArrayList<>();

        for (Message targetMessage : messageList) {
            if (targetMessage.getChannel().getChannelName().equals(message.getChannel().getChannelName())) {
                messageByChannel.add(targetMessage);
            }
        }
        return messageByChannel;
    }
    public List<Message> findAllMessage() {
        List<Message> messageList = fileManager.allLoad(directory);
        return new ArrayList<>(messageList);
    }


    // 프린트
    public void printMessage(Message message) {
        System.out.println(message);
    }
    public void printMessagesList(List<Message> messages) {
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    // 삭제
    public void deleteMessage(Message message) {
        List<Message> messageList = fileManager.allLoad(directory);

        for (Message targetMessage : messageList) {
            if (targetMessage.getId().equals(message.getId())) {
                System.out.println(message.getContent() + " content message deleted");
                fileManager.delete(directory.resolve(targetMessage.getId().toString().concat(FILE_EXTENSION)));
            }
        }
    }
}
