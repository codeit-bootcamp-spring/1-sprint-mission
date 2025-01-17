package com.sprint.mission.jcf;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.MessageService;

import java.util.ArrayList;
import java.util.List;


public class JCFMessageService implements MessageService {
    private static JCFMessageService instance;
    private final List<Message> messageData = new ArrayList<>();

    private JCFMessageService() {}

    public static JCFMessageService getInstance() {
        if (instance == null) {
            instance = new JCFMessageService();
        }
        return instance;
    }


    // 메시지 생성
    @Override
    public Message createMessage(User user, Channel channel, String message) {

        for (Message existingMessage : messageData) {
            if (existingMessage.getName().equals(user) && existingMessage.getMessage().equals(message)) {
                System.out.println("이미 메시지가 존재합니다.");
                return null;
            }
        }
        Message newMessage = new Message(user, channel, message);
        messageData.add(newMessage);
        System.out.println("메시지가 생성되었습니다.\n");
        printMessage(newMessage);
        return newMessage;
    }


    // 메시지 변경
    @Override
    public void updateMessage(User user, Channel channel,String Message, String modifiedMessage) {
        for (Message message : messageData) {
            if (!message.equals(modifiedMessage) && message.getName().equals(user) && message.getMessage().equals(Message)) {
                message.setMessage(modifiedMessage);
                System.out.println("메시지가 변경 변경되었습니다.");
                printMessage(message);
                return;
            } else {
                System.out.println("메시지를 찾을 수 없습니다.\n");
            }
        }
    }

    // 유저의 모든 메시지 조회
    @Override
    public List<Message> getAllMessageList() {
        for (Message message : messageData) {
            System.out.println("메시지 전체 조회");
            printMessage(message);
        } return messageData;
    }

    // 특정 유저 메시지 조회
    @Override
    public void printChannelMessage(User user) {
        for (Message message : messageData) {
            if (message.getName().equals(user)) {
                System.out.println("사용자의 메시지 조회");
                printMessage(message);
            }
        }
    }

    // 메시지 삭제, 메시지를 찾을 수 없을 경우 예외 처리
    @Override
    public void deleteMessage(String messageContent) {
        Message messageToDelete = null;
        for (Message message : messageData) {
            if (message.getMessage().equals(messageContent)) {
                messageToDelete = message;
                break;
            }
        }

        if (messageToDelete != null) {
            messageData.remove(messageToDelete);
            System.out.println("메시지가 삭제되었습니다:");
            printMessage(messageToDelete);
        } else {
            System.out.println("메시지를 찾을 수 없습니다.\n");
        }
    }
    // 메시지 출력 포맷팅
    private void printMessage(Message message) {
        System.out.println(" - 사용자: " + message.getName().getName());
        System.out.println(" - 이메일: " + message.getName().getEmail());
        System.out.println(" - 채널: " + message.getChannel().getChannelName());
        System.out.println(" - 메시지: " + message.getMessage());
        System.out.println(" - 생성 시간: " + message.getName().getFormattedCreatedAt() + "\n");
    }
}