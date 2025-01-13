package com.sprint.mission.jcf;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.MessageService;

import java.util.ArrayList;
import java.util.List;


public class JCFMessageService implements MessageService {

    private final List<Message> messageData = new ArrayList<>();

    // 메시지 생성
    @Override
    public Message createMessage(User user, Channel channel, String message) {

        for (Message existingMessage : messageData) {
            if (existingMessage.getUser().equals(user) && existingMessage.getMessage().equals(message)) {
                System.out.println("이미 메시지가 존재합니다.");
                return null;
            }
        }
        Message newMessage = new Message(user, channel, message);
        messageData.add(newMessage);
        System.out.println(user + "님이 " + channel + "에 " +
                "메시지 생성 : " + newMessage + "\n메시지가 성공적으로 생성되었습니다!\n");
        return newMessage;
    }


    // 메시지 변경
    @Override
    public void updateMessage(User user, Channel channel,String Message, String modifiedMessage) {
        for (Message message : messageData) {
            if (!message.equals(modifiedMessage) && message.getUser().equals(user) && message.getMessage().equals(Message)) {
                message.setMessage(modifiedMessage);
                System.out.println("메시지 변경 : " + modifiedMessage + "\n메시지가 변경되었습니다.\n");
                return;
            } else {
                throw new IllegalArgumentException("메시지를 찾을 수 없습니다.");
            }
        }
    }

    // 유저의 모든 메시지 조회
    @Override
    public List<Message> getAllMessageList() {
        System.out.println("메시지 목록" + messageData);
        return new ArrayList<>(messageData);
    }

    // 특정 유저 메시지 조회
    @Override
    public void printChannelMessage(User user) {
        System.out.println(messageData);
    }

    // 메시지 삭제, 메시지를 찾을 수 없을 경우 예외 처리
    @Override
    public void deleteMessage(String message) {
        Message messageToDelete = null;
        for (Message message1 : messageData) {
            if (message1.getMessage().equals(message)) {
                messageToDelete = message1;
                break;
            }
        }
        if (messageToDelete != null && messageData.contains(messageToDelete)) {
            messageData.remove(messageToDelete);
            System.out.println("\n메시지 삭제 : " + message + "\n메시지가 성공적으로 삭제되었습니다.\n");
        } else{
            throw new IllegalArgumentException("메시지를 찾을 수 없습니다.");
        }
    }
}