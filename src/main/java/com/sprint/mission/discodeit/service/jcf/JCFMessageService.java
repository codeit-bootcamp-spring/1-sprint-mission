package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final List<Message> data;

    public JCFMessageService() {
        data = new ArrayList<>();
    }

    // 메세지 생성
    @Override
    public Message createMessage(Channel channel, User writer, String content) {
        if (inValidContent(content)) {
            Message newMessage = new Message(channel, writer, content);
            data.add(newMessage);
            System.out.println(channel.getTitle() + " channel: " + writer.getName() + " send new message");
            return newMessage;
        }
        return null;
    }

    // 메세지 모두 조회
    @Override
    public List<Message> getAllMessageList() {
        return data;
    }

    // id로 메세지 조회
    @Override
    public Message searchById(UUID messageId) {
        for (Message message : data) {
            if (message.getId().equals(messageId)) {
                return message;
            }
        }
        System.out.println("message dose not exist");
        return null;
    }

    @Override
    public void printMessageInfo(Message message) {
        System.out.println(message);
    }

    @Override
    public void printMessageListInfo(List<Message> messageList) {
        for (Message message : messageList) {
            printMessageInfo(message);
        }
    }

    // 메세지 수정
    @Override
    public void updateMessage(Message message, String content) {
        if (inValidContent(content)) {
            message.updateContent(content);
            System.out.println("success update");
        }
    }

    // 메세지 삭제
    @Override
    public void deleteMessage(Message message) {
        data.remove(message);
        System.out.println("success delete");
    }

    // 메세지 검사
    private boolean inValidContent(String content) {
        if (content.isBlank()) {
            System.out.println("content must not be blank");
            return false;
        }
        return true;
    }
}
