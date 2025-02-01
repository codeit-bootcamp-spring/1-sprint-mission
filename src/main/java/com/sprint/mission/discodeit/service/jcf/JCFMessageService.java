package com.sprint.mission.discodeit.service.jcf;

import java.util.List;
import java.util.ArrayList;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;


public class JCFMessageService implements MessageService {
    private final List<Message> data;

    public JCFMessageService() {
        data = new ArrayList<>();
    }

    // 생성
    public Message createMessage(String content, Channel channel, User sender) {
        Message message = new Message(content, channel, sender);
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
    public Message findMessageById(Message m) {
        for (Message message : data) {
            if (message.getId().equals(m.getId())) {
                System.out.println("message found");
                return message;
            }
        }
        System.out.println("message not found");
        return null;
    }
    public List<Message> findMessageByChannel(Message message) {
        List<Message> messageByChannel = new ArrayList<>();
        for (Message m : data) {
            if (m.getChannel().equals(message.getChannel())) {
                messageByChannel.add(m);
            }
        }
        return messageByChannel;
    }
    public List<Message> findAllMessage() {
        return data;
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
        System.out.println(message.getContent() + " content message deleted");
        data.remove(message);
    }

}
