package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {
    private static final String FILE_PATH = "tmp/message.ser";
    private Map<UUID, Message> data;
    private static volatile FileMessageService messageService;

    public FileMessageService() {
        this.data= loadDataFromFile();
    }

    public static FileMessageService getInstance() {
        if(messageService == null) {
            synchronized (FileMessageService.class) {
                if(messageService == null) {
                    messageService = new FileMessageService();
                }
            }

        }
        return messageService;
    }
    private Map<UUID, Message> loadDataFromFile() {
        File file = new File(FILE_PATH);
        if(!file.exists()){
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드에 실패했습니다 : " + e.getMessage());
        }
    }
    private void saveDataToFile() {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("파일에 데이터 저장 작업을 실패했습니다." + e.getMessage());
        }
    }

    @Override
    public Message createMessage(UUID channelId, UUID authorId, String content) {
        if(isContent(content)) {
            Message newMessage = new Message(content,channelId, authorId);
            data.put(newMessage.getId(), newMessage);
            return newMessage;
        }
        return null;
    }
    private boolean isContent(String content){
        if(content.isBlank()) {
            System.out.println("내용을 입력해주세요!!");
            return false;
        }
        return true;
    }
    @Override
    public List<Message> getAllMessageList() {
        return data.values().stream().collect(Collectors.toList());
    }

    @Override
    public Message searchById(UUID messageId) {
        Message message = data.get(messageId);
        if(message == null) {
            System.out.println("메시지가 없습니다.");
        }
        return message;
    }

    @Override
    public void updateMessage(UUID messageId, String content) {
        Message message = data.get(messageId);
        if(message == null) {
            System.out.println("수정할 메시지가 존재하지 않습니다.");
            return;
        }
        if(isContent(content)) {
            message.setContent(content);
            saveDataToFile();
            System.out.println("메시지가 성공적으로 수정되었습니다 : " + content);
        }

    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = data.remove(messageId);
        if(message == null) {
            System.out.println("삭제할 메시지가 존재하지 않습니다.");
        } else {
            saveDataToFile();
            System.out.println("메시지가 성공적으로 삭제되었습니다.");
        }

    }

}
