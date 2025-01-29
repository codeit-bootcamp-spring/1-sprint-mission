package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {

    private final String fileName = "savedata/message.ser";
    private final Map<UUID, Message> messageList;

    public FileMessageService() {
        this.messageList = loadFile();
    }

    @Override
    public Message createMessage(UUID userId, UUID channelId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("메시지 내용은 null 또는 빈 문자열일 수 없습니다.");
        }
        Message message = new Message(userId, channelId, content);
        messageList.put(message.getMsgId(), message);
        System.out.println("메시지가 생성되었습니다: " + content);
        saveFile();
        return message;
    }

    @Override
    public Message readMessage(UUID msgID) {
        return Optional.ofNullable(messageList.get(msgID))
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다. ID: " + msgID));
    }

    @Override
    public List<Message> readAllMessage() {
        return new ArrayList<>(messageList.values());
    }

    @Override
    public Message modifyMessage(UUID msgID, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("메시지 내용은 null 또는 빈 문자열일 수 없습니다.");
        }
        Message message = readMessage(msgID);
        String oriContent = message.getContent();
        message.updateContent(content);
        message.updateUpdatedAt();
        System.out.println("메시지가 변경되었습니다: \"" + oriContent + "\" -> \"" + content + "\"");
        saveFile();
        return message;
    }

    @Override
    public void deleteMessage(UUID msgID) {


        Message message = readMessage(msgID);
        if (messageList.remove(msgID) != null) {
            saveFile();
            System.out.println("메시지가 삭제되었습니다: " + message.getContent());
        } else {
            System.out.println("메시지 삭제 실패: ID " + msgID);
        }
    }


    //저장로직
    public void saveFile(){
        File file = new File(fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream
                (new FileOutputStream(fileName))) {
            oos.writeObject(messageList);

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패 : " + e.getMessage(), e);
        }

    }


    public Map<UUID, Message> loadFile(){

        File file = new File(fileName);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream
                (new FileInputStream(file)))
        {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드 실패 : " + e.getMessage(), e);
        }
    }
    
    
}
