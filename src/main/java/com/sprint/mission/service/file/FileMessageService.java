package com.sprint.mission.service.file;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.MessageService;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileMessageService implements MessageService {
    private static final String FILE_PATH = "src/main/resources/message.ser";
    private Map<User, Message> messageData;

    public FileMessageService(){
        this.messageData = loadData();
    }

    // 메시지 생성
    @Override
    public Message createMessage(User user, Channel channel, String message) {
        Message newMessage = new Message(user, channel, message);
        if(message == null){
            System.out.println("\n**메시지를 입력해주세요.**");
        }
        messageData.put(user, newMessage);
        saveData();
        System.out.println("\n***메시지가 생성되었습니다.***");
        return newMessage;
    }


    // 메시지 변경
    @Override
    public void updateMessage(User user, Channel channel,String Message, String modifiedMessage) {
        Message message = messageData.get(user);
        if(messageData.containsValue(message) || message.equals(modifiedMessage)){
            System.out.println("\n**메시지를 확인해주세요.**");
        }
        if(message == null) {
            System.out.println("\n**메시지를 찾을 수 없습니다.**");
        }
        message.setMessage(modifiedMessage);
        saveData();
        System.out.println("\n***메시지가 변경되었습니다.***");
    }

    // 유저의 모든 메시지 조회
    @Override
    public List<Message> findAllMessageList() {
        System.out.println("\n***메시지 전체 조회***");
        List<Message> messages = new ArrayList<>(messageData.values());
        for (Message message : messages) {
            System.out.println(message);
        }
        return messages;
    }

    // 특정 유저 메시지 조회
    @Override
    public void printChannelMessage(User user) {
        for (Message message : messageData.values()) {
            if (message.getName().equals(user)) {
                System.out.println("\n***[사용자의 메시지 조회]***");
                System.out.println(message);
            }
        }
    }

    // 메시지 삭제, 메시지를 찾을 수 없을 경우 예외 처리
    @Override
    public void deleteMessage(String message) {
        User userToDelete = null;
        for (Map.Entry<User, Message> entry : messageData.entrySet()) {
            if (entry.getValue().getMessage().equals(message)) {
                userToDelete = entry.getKey();
                break;
            }
        }
        if(userToDelete != null) {
            messageData.remove(userToDelete);
            saveData();
            System.out.println("\n***메시지 삭제 성공***");
        } else {
            System.out.println("\n**메시지를 찾을 수 없습니다.**");
        }
    }

    // 파일에 데이터 저장
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messageData);
            System.out.println("===데이터가 저장되었습니다: " + FILE_PATH + "===");
        } catch (IOException e) {
            System.err.println("===데이터 저장 중 오류 발생: " + e.getMessage() + "===");
        }
    }

    private Map<User, Message> loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("===파일이 존재하지 않음. 새로 생성됩니다.===");
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<User, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("===파일 로드 중 오류 발생: " + e.getMessage() + "===");
            return new HashMap<>();
        }
    }

    public void resetData() {
        this.messageData = new HashMap<>();
        saveData();
        System.out.println("===데이터가 초기화되어 빈 상태로 저장되었습니다.===");
    }

}