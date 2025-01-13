package com.sprint.mission.discodeit.service.jcf;


import java.util.ArrayList;
import java.util.UUID;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;


public class JCFMessageService implements MessageService {
    private final ArrayList<Message> data;

    public JCFMessageService() {
        data = new ArrayList<>();
    }

    // 생성
    public Message createMessage(String content, Channel channel, User sender) {
        UUID uuid = UUID.randomUUID();
        long timestamp = System.currentTimeMillis();
        Message message = new Message(uuid, content, channel, sender, timestamp);
        data.add(message);
        System.out.println(channel.getChannelName() + " channel " + sender.getUsername() + " : " + content);
        return message;
    }
    // 메세지 수정
    public Message updateContent(Message message, String content) {
        message.updateContent(content);
        System.out.println("content updated");
        return message;
    }

    // 조회
    public Message findMessageById(UUID id) {
        for (Message message : data) {
            if (message.getId().equals(id)) {
                System.out.println("message found");
                return message;
            }
        }
        System.out.println("message not found");
        return null;
    }
    public ArrayList<Message> findAllMessage() {
        return data;
    }

    // 프린트
    public void printMessage(Message message) {
        System.out.println(message);
    }
    public void printChannelMessage(Message message, ArrayList<Message> messagesList) {
        for (Message messages : messagesList) {
            if (messages.getChannel().equals(message.getChannel())) {
                System.out.println(messages);
            }
        }
    }
    public void printAllMessages(ArrayList<Message> messages) {
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    // 삭제
    public void deleteMessage(Message message) {
        System.out.println(message.getContent() + " content message deleted");
        data.remove(message);
    }

}
