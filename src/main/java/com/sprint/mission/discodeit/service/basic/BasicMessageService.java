package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // 생성
    public Message createMessage(String content, Channel channel, User sender) {
        Message message = new Message(content, channel, sender);
        if (messageRepository.saveMessage(message)) {
            System.out.println(channel.getChannelName() + " channel " + sender.getUsername() + " : " + content);
            return message;
        }
        return null;
    }

    // 내용 수정
    public Message updateContent(Message message, String content) {
        Message updateMessage = messageRepository.loadMessage(message);
        if (updateMessage != null) {
            updateMessage.updateContent(content);
            if (messageRepository.saveMessage(updateMessage)) {
                System.out.println("content updated");
                return updateMessage;
            }
        }
        return null;
    }

    // 조회
    public Message findMessageById(Message m) {
        return messageRepository.loadMessage(m);
    }
    public List<Message> findMessageByChannel(Message message) {
        return messageRepository.getMessagesByChannel(message);
    }
    public List<Message> findAllMessage() {
        return messageRepository.loadAllMessages();
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
        if (messageRepository.deleteMessage(message)) {
            System.out.println(message.getContent() + " content message deleted");
        }
    }
}
