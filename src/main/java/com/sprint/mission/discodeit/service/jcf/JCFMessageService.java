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

    // Create Message
    @Override
    public Message createMessage(Channel channel, User writer, String content) {
        if (isContent(content)) {
            Message newMessage = new Message(channel, writer, content);
            data.add(newMessage);
            System.out.println(channel.getTitle() + " channel: " + writer.getName() + " 님께서 새로운 메시지를 보내셨습니다.");
            return newMessage;
        }
        return null;
    }

    // Read all message
    @Override
    public List<Message> getAllMessageList() {
        return data;
    }

    // id 받아서 -> Read that message
    @Override
    public Message searchById(UUID messageId) {
        for (Message message : data) {
            if (message.getId().equals(messageId)) {
                return message;
            }
        }
        System.out.println("message가 존재하지 않습니다.");
        return null;
    }

    //message 정보 출력하기
    @Override
    public void printMessageInfo(Message message) {
        System.out.println(message);
    }

    //message정보 리스트로 출력하기
    @Override
    public void printMessageListInfo(List<Message> messageList) {
        for (Message message : messageList) {
            printMessageInfo(message);
        }
    }

    // update Message Content
    @Override
    public void updateMessage(Message message, String content) {
        if (isContent(content)) {
            message.updateContent(content);
            System.out.println("수정되었습니다");
        }
    }

    // Delete Message
    @Override
    public void deleteMessage(Message message) {
        data.remove(message);
        System.out.println("삭제된 메세지입니다.");
    }

    // 유효성 검사 -> message에 내용이 들어가있는지?
    private boolean isContent(String content) {
        if (content.isBlank()) {
            System.out.println("내용을 입력해주세요!!!");
            return false;
        }
        return true;
    }
}